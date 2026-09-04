package com.civicpulse.service.config;

import com.civicpulse.service.domain.Application;
import com.civicpulse.service.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ApplicationRepository applicationRepository;

    public DataInitializer(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding database with default applications...");

        if (applicationRepository.count() == 0) {
            // Seed Priya Sharma's birth certificate application matching Milestone 2 expected output
            Application priyaBirthCertApp = Application.builder()
                    .id(UUID.fromString("9a117b4c-9f20-4e4b-9d41-e940f8fa8472"))
                    .applicationNumber("APP-2024-1247")
                    .citizenId(UUID.fromString("49b92718-47e5-4ebc-ba3d-abfa7b2a60cc"))
                    .citizenName("Priya Sharma")
                    .type(Application.Type.BIRTH_CERTIFICATE)
                    .status(Application.Status.DOCUMENT_VERIFIED)
                    .metadata("Child: Aarav, DOB: 15-May-2026")
                    .appliedDate(LocalDateTime.now().minusDays(3))
                    .isVerified(true)
                    .build();
            applicationRepository.save(priyaBirthCertApp);

            // Seed an unverified Trade License application
            Application tradeLicenseApp = Application.builder()
                    .id(UUID.fromString("c48b1111-2222-3333-4444-555566668888"))
                    .applicationNumber("APP-2026-8888")
                    .citizenId(UUID.fromString("36a83693-39f5-47ec-a63e-dbfa7b2a60bb"))
                    .citizenName("Ramesh Kumar")
                    .type(Application.Type.TRADE_LICENSE)
                    .status(Application.Status.SUBMITTED)
                    .metadata("Business: Ramesh Kirana, Type: Retail")
                    .appliedDate(LocalDateTime.now().minusDays(1))
                    .isVerified(false)
                    .build();
            applicationRepository.save(tradeLicenseApp);

            log.info("Application database seeding complete.");
        } else {
            log.info("Application database already contains data. Skipping seeding.");
        }
    }
}
