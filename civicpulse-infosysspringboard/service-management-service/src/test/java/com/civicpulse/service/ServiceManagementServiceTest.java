package com.civicpulse.service;

import com.civicpulse.service.domain.Application;
import com.civicpulse.service.domain.Certificate;
import com.civicpulse.service.domain.Permit;
import com.civicpulse.service.repository.ApplicationRepository;
import com.civicpulse.service.repository.CertificateRepository;
import com.civicpulse.service.repository.PermitRepository;
import com.civicpulse.service.service.ServiceManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceManagementServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private PermitRepository permitRepository;

    @InjectMocks
    private ServiceManagementService serviceManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitApplication_GeneratesApplicationNumber() {
        Application app = Application.builder()
                .citizenId(UUID.randomUUID())
                .citizenName("Priya Sharma")
                .type(Application.Type.BIRTH_CERTIFICATE)
                .metadata("Child: Aarav")
                .build();

        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Application result = serviceManagementService.submitApplication(app);

        assertNotNull(result);
        assertNotNull(result.getApplicationNumber());
        assertTrue(result.getApplicationNumber().startsWith("APP-2026-"));
        assertEquals(Application.Status.SUBMITTED, result.getStatus());
        assertFalse(result.isVerified());
    }

    @Test
    void verifyDocuments_ChangesStatus() {
        UUID appId = UUID.randomUUID();
        Application app = Application.builder()
                .id(appId)
                .applicationNumber("APP-2024-1247")
                .citizenId(UUID.randomUUID())
                .citizenName("Priya Sharma")
                .status(Application.Status.SUBMITTED)
                .isVerified(false)
                .build();

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(app));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Application result = serviceManagementService.verifyDocuments(appId);

        assertNotNull(result);
        assertTrue(result.isVerified());
        assertEquals(Application.Status.DOCUMENT_VERIFIED, result.getStatus());
    }

    @Test
    void approveApplication_BirthCertificate_GeneratesCertificate() {
        UUID appId = UUID.randomUUID();
        Application app = Application.builder()
                .id(appId)
                .applicationNumber("APP-2024-1247")
                .citizenId(UUID.randomUUID())
                .citizenName("Priya Sharma")
                .type(Application.Type.BIRTH_CERTIFICATE)
                .status(Application.Status.DOCUMENT_VERIFIED)
                .isVerified(true)
                .build();

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(app));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(certificateRepository.findByApplicationId(appId)).thenReturn(Optional.empty());

        Application result = serviceManagementService.approveApplication(appId);

        assertNotNull(result);
        assertEquals(Application.Status.APPROVED, result.getStatus());
        verify(certificateRepository, times(1)).save(any(Certificate.class));
    }

    @Test
    void trackDownload_IncrementsCount() {
        UUID certId = UUID.randomUUID();
        Certificate cert = Certificate.builder()
                .id(certId)
                .certificateNumber("BC-2024-1247")
                .downloadCount(0)
                .build();

        when(certificateRepository.findById(certId)).thenReturn(Optional.of(cert));
        when(certificateRepository.save(any(Certificate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Certificate result = serviceManagementService.trackDownload(certId);

        assertNotNull(result);
        assertEquals(1, result.getDownloadCount());
        verify(certificateRepository, times(1)).save(any(Certificate.class));
    }
}
