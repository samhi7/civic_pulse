package com.civicpulse.citizen.config;

import com.civicpulse.citizen.domain.Citizen;
import com.civicpulse.citizen.repository.CitizenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CitizenRepository citizenRepository;

    public DataInitializer(CitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding database with default citizens...");

        if (citizenRepository.count() == 0) {
            Citizen ramesh = Citizen.builder()
                    .id(UUID.fromString("36a83693-39f5-47ec-a63e-dbfa7b2a60bb"))
                    .name("Ramesh Kumar")
                    .email("ramesh.kumar@example.com")
                    .phone("+91-9876543210")
                    .aadharNumber("1234-5678-9012")
                    .ward("Ward 12")
                    .registeredAt(LocalDateTime.now().minusDays(10))
                    .build();
            citizenRepository.save(ramesh);

            Citizen priya = Citizen.builder()
                    .id(UUID.fromString("49b92718-47e5-4ebc-ba3d-abfa7b2a60cc"))
                    .name("Priya Sharma")
                    .email("priya.sharma@example.com")
                    .phone("+91-9988776655")
                    .aadharNumber("9876-5432-1098")
                    .ward("Ward 8")
                    .registeredAt(LocalDateTime.now().minusDays(5))
                    .build();
            citizenRepository.save(priya);

            Citizen vikram = Citizen.builder()
                    .id(UUID.fromString("e3d748f2-824f-4d37-8ff0-d128cbda9e11"))
                    .name("Vikram Malhotra")
                    .email("vikram.m@example.com")
                    .phone("+91-9123456789")
                    .aadharNumber("5544-3322-1100")
                    .ward("Ward 4")
                    .registeredAt(LocalDateTime.now().minusDays(15))
                    .build();
            citizenRepository.save(vikram);

            Citizen sunita = Citizen.builder()
                    .id(UUID.fromString("fa7a8b9c-10d9-4fa2-8b3d-cbda9e112233"))
                    .name("Sunita Rao")
                    .email("sunita.rao@example.com")
                    .phone("+91-9345678901")
                    .aadharNumber("8877-6655-4433")
                    .ward("Ward 9")
                    .registeredAt(LocalDateTime.now().minusDays(8))
                    .build();
            citizenRepository.save(sunita);

            Citizen amit = Citizen.builder()
                    .id(UUID.fromString("bc8a7c6b-1234-4567-89ab-cdef01234567"))
                    .name("Amit Patel")
                    .email("amit.patel@example.com")
                    .phone("+91-9456789012")
                    .aadharNumber("4455-6677-8899")
                    .ward("Ward 15")
                    .registeredAt(LocalDateTime.now().minusDays(3))
                    .build();
            citizenRepository.save(amit);

            log.info("Citizen database seeding complete.");
        } else {
            log.info("Citizen database already contains data. Skipping seeding.");
        }
    }
}
