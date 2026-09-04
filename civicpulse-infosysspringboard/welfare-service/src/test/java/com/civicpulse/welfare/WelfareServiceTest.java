package com.civicpulse.welfare;

import com.civicpulse.welfare.domain.Beneficiary;
import com.civicpulse.welfare.domain.WelfareScheme;
import com.civicpulse.welfare.repository.BeneficiaryRepository;
import com.civicpulse.welfare.repository.WelfareSchemeRepository;
import com.civicpulse.welfare.service.WelfareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WelfareServiceTest {

    @Mock
    private WelfareSchemeRepository welfareSchemeRepository;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private WelfareService welfareService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createScheme_SavesScheme() {
        WelfareScheme scheme = WelfareScheme.builder()
                .name("PM Awas Yojana")
                .allocatedAmount(new BigDecimal("2400000.00"))
                .disbursedAmount(BigDecimal.ZERO)
                .build();

        when(welfareSchemeRepository.save(any(WelfareScheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WelfareScheme result = welfareService.createScheme(scheme);

        assertNotNull(result);
        assertEquals("PM Awas Yojana", result.getName());
        verify(welfareSchemeRepository, times(1)).save(scheme);
    }

    @Test
    void enrollBeneficiary_ChecksDuplicateAndSaves() {
        UUID schemeId = UUID.randomUUID();
        UUID citizenId = UUID.randomUUID();

        Beneficiary beneficiary = Beneficiary.builder()
                .schemeId(schemeId)
                .citizenId(citizenId)
                .citizenName("Ramesh Kumar")
                .build();

        when(beneficiaryRepository.findBySchemeIdAndCitizenId(schemeId, citizenId)).thenReturn(Optional.empty());
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Beneficiary result = welfareService.enrollBeneficiary(beneficiary);

        assertNotNull(result);
        assertEquals(Beneficiary.Status.PENDING, result.getStatus());
        assertFalse(result.isVerified());
        verify(beneficiaryRepository, times(1)).save(beneficiary);
    }

    @Test
    void verifyEligibility_UpdatesVerification() {
        UUID beneficiaryId = UUID.randomUUID();
        Beneficiary beneficiary = Beneficiary.builder()
                .id(beneficiaryId)
                .citizenName("Ramesh Kumar")
                .status(Beneficiary.Status.PENDING)
                .isVerified(false)
                .build();

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Beneficiary result = welfareService.verifyEligibility(beneficiaryId);

        assertNotNull(result);
        assertTrue(result.isVerified());
        assertEquals(Beneficiary.Status.APPROVED, result.getStatus());
    }

    @Test
    void disburseFunds_IncrementsSchemeAndBeneficiaryAmounts() {
        UUID schemeId = UUID.randomUUID();
        WelfareScheme scheme = WelfareScheme.builder()
                .id(schemeId)
                .name("PM Awas Yojana")
                .allocatedAmount(new BigDecimal("2400000.00"))
                .disbursedAmount(new BigDecimal("2100000.00"))
                .build();

        UUID beneficiaryId = UUID.randomUUID();
        Beneficiary beneficiary = Beneficiary.builder()
                .id(beneficiaryId)
                .schemeId(schemeId)
                .citizenName("Ramesh Kumar")
                .status(Beneficiary.Status.APPROVED)
                .disbursedAmount(BigDecimal.ZERO)
                .isVerified(true)
                .build();

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(welfareSchemeRepository.findById(schemeId)).thenReturn(Optional.of(scheme));
        when(welfareSchemeRepository.save(any(WelfareScheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Beneficiary result = welfareService.disburseFunds(beneficiaryId, new BigDecimal("2400.00"));

        assertNotNull(result);
        assertEquals(Beneficiary.Status.DISBURSED, result.getStatus());
        assertEquals(new BigDecimal("2400.00"), result.getDisbursedAmount());
        assertEquals(new BigDecimal("2102400.00"), scheme.getDisbursedAmount());
        verify(welfareSchemeRepository, times(1)).save(scheme);
    }
}
