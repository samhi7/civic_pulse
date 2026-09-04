package com.civicpulse.welfare.service;

import com.civicpulse.welfare.domain.Beneficiary;
import com.civicpulse.welfare.domain.WelfareScheme;
import com.civicpulse.welfare.repository.BeneficiaryRepository;
import com.civicpulse.welfare.repository.WelfareSchemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WelfareService {

    private static final Logger log = LoggerFactory.getLogger(WelfareService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    private final WelfareSchemeRepository welfareSchemeRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public WelfareService(WelfareSchemeRepository welfareSchemeRepository, BeneficiaryRepository beneficiaryRepository) {
        this.welfareSchemeRepository = welfareSchemeRepository;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public WelfareScheme createScheme(WelfareScheme scheme) {
        WelfareScheme saved = welfareSchemeRepository.save(scheme);
        log.info("Welfare scheme created: {} (ID: {})", saved.getName(), saved.getId());
        return saved;
    }

    public Beneficiary enrollBeneficiary(Beneficiary beneficiary) {
        // Check duplicate enrollment
        Optional<Beneficiary> existing = beneficiaryRepository.findBySchemeIdAndCitizenId(
                beneficiary.getSchemeId(), beneficiary.getCitizenId()
        );
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Citizen is already enrolled in this scheme.");
        }

        beneficiary.setStatus(Beneficiary.Status.PENDING);
        beneficiary.setDisbursedAmount(BigDecimal.ZERO);
        beneficiary.setVerified(false);

        Beneficiary saved = beneficiaryRepository.save(beneficiary);
        log.info("Citizen {} enrolled in scheme {}", saved.getCitizenName(), saved.getSchemeId());
        return saved;
    }

    public Beneficiary verifyEligibility(UUID id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiary enrollment not found."));

        beneficiary.setVerified(true);
        beneficiary.setStatus(Beneficiary.Status.APPROVED);
        log.info("Eligibility verified for beneficiary citizen {}", beneficiary.getCitizenName());
        return beneficiaryRepository.save(beneficiary);
    }

    public Beneficiary disburseFunds(UUID id, BigDecimal amount) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiary enrollment not found."));

        // Verify eligibility if not already done
        if (!beneficiary.isVerified()) {
            beneficiary.setVerified(true);
        }

        // Set status to DISBURSED and update disbursed amount
        beneficiary.setStatus(Beneficiary.Status.DISBURSED);
        beneficiary.setDisbursedAmount(beneficiary.getDisbursedAmount().add(amount));
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);

        // Update the Welfare Scheme balances
        WelfareScheme scheme = welfareSchemeRepository.findById(beneficiary.getSchemeId())
                .orElseThrow(() -> new IllegalArgumentException("Scheme not found."));
        scheme.setDisbursedAmount(scheme.getDisbursedAmount().add(amount));
        welfareSchemeRepository.save(scheme);

        log.info("Disbursed funds {} to citizen {}. Scheme utilization updated.", amount, beneficiary.getCitizenName());

        // Notify Budget Service via REST call to log expenditure against the Housing Dept budget
        notifyBudgetServiceOfDisbursement(beneficiary.getCitizenName(), amount);

        return savedBeneficiary;
    }

    public List<WelfareScheme> getAllSchemes() {
        return welfareSchemeRepository.findAll();
    }

    public Optional<WelfareScheme> getSchemeById(UUID id) {
        return welfareSchemeRepository.findById(id);
    }

    public List<Beneficiary> getBeneficiariesForScheme(UUID schemeId) {
        return beneficiaryRepository.findBySchemeId(schemeId);
    }

    private void notifyBudgetServiceOfDisbursement(String citizenName, BigDecimal amount) {
        try {
            String budgetServiceUrl = String.format(
                    "http://localhost:8085/api/v1/budgets/record-external-expense?department=Housing&amount=%s&description=Disbursement to %s",
                    amount.toPlainString(), citizenName
            );
            log.info("Sending inter-service REST request to Budget Service: {}", budgetServiceUrl);
            restTemplate.postForObject(budgetServiceUrl, null, Void.class);
            log.info("Budget Service logged disbursement expense successfully.");
        } catch (Exception e) {
            log.warn("Failed to notify Budget Service (Service might be offline): {}", e.getMessage());
        }
    }
}
