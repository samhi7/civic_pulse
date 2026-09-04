package com.civicpulse.welfare.config;

import com.civicpulse.welfare.domain.Beneficiary;
import com.civicpulse.welfare.domain.WelfareScheme;
import com.civicpulse.welfare.repository.BeneficiaryRepository;
import com.civicpulse.welfare.repository.WelfareSchemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final WelfareSchemeRepository welfareSchemeRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public DataInitializer(WelfareSchemeRepository welfareSchemeRepository, BeneficiaryRepository beneficiaryRepository) {
        this.welfareSchemeRepository = welfareSchemeRepository;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding database with default welfare schemes...");

        if (welfareSchemeRepository.count() == 0) {
            // Seed PM Awas Yojana matching Milestone 3 expected output
            WelfareScheme awasYojana = WelfareScheme.builder()
                    .id(UUID.fromString("8a117b4c-9f20-4e4b-9d41-e940f8fa8473"))
                    .name("PM Awas Yojana")
                    .description("Affordable housing scheme for urban and rural poor.")
                    .allocatedAmount(new BigDecimal("2400000.00")) // $2.4M
                    .disbursedAmount(new BigDecimal("2100000.00")) // $2.1M
                    .active(true)
                    .build();
            welfareSchemeRepository.save(awasYojana);

            // Seed Smart Education Scheme
            WelfareScheme educationScheme = WelfareScheme.builder()
                    .id(UUID.fromString("b48b1111-2222-3333-4444-555566669999"))
                    .name("Smart Education Grant")
                    .description("Scholarship grants for higher education students.")
                    .allocatedAmount(new BigDecimal("4000000.00")) // $4.0M
                    .disbursedAmount(new BigDecimal("3100000.00")) // $3.1M
                    .active(true)
                    .build();
            welfareSchemeRepository.save(educationScheme);

            // Seed Ramesh Kumar as an approved beneficiary of PM Awas Yojana (awaiting payment)
            Beneficiary rameshBeneficiary = Beneficiary.builder()
                    .id(UUID.fromString("6a117b4c-9f20-4e4b-9d41-e940f8fa8474"))
                    .schemeId(awasYojana.getId())
                    .citizenId(UUID.fromString("36a83693-39f5-47ec-a63e-dbfa7b2a60bb"))
                    .citizenName("Ramesh Kumar")
                    .status(Beneficiary.Status.APPROVED)
                    .disbursedAmount(BigDecimal.ZERO)
                    .eligibilityCriteria("Income below threshold, no brick house registered.")
                    .isVerified(true)
                    .build();
            beneficiaryRepository.save(rameshBeneficiary);

            // Seed Priya Sharma as a pending beneficiary of Smart Education
            Beneficiary priyaBeneficiary = Beneficiary.builder()
                    .id(UUID.fromString("f48b1111-2222-3333-4444-555566669990"))
                    .schemeId(educationScheme.getId())
                    .citizenId(UUID.fromString("49b92718-47e5-4ebc-ba3d-abfa7b2a60cc"))
                    .citizenName("Priya Sharma")
                    .status(Beneficiary.Status.PENDING)
                    .disbursedAmount(BigDecimal.ZERO)
                    .eligibilityCriteria("Merit scholarship score above 90%")
                    .isVerified(false)
                    .build();
            beneficiaryRepository.save(priyaBeneficiary);

            log.info("Welfare database seeding complete.");
        } else {
            log.info("Welfare database already contains data. Skipping seeding.");
        }
    }
}
