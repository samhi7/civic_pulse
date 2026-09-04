package com.civicpulse.grievance.config;

import com.civicpulse.grievance.domain.Grievance;
import com.civicpulse.grievance.repository.GrievanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final GrievanceRepository grievanceRepository;

    public DataInitializer(GrievanceRepository grievanceRepository) {
        this.grievanceRepository = grievanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding database with default grievances...");

        if (grievanceRepository.count() == 0) {
            // Seed Ramesh Kumar's water supply grievance matching Milestone 1 expected output
            Grievance waterSupplyGrievance = Grievance.builder()
                    .id(UUID.fromString("6a117b4c-9f20-4e4b-9d41-e940f8fa8471"))
                    .citizenId(UUID.fromString("36a83693-39f5-47ec-a63e-dbfa7b2a60bb"))
                    .citizenName("Ramesh Kumar")
                    .title("Water Supply Disruption")
                    .description("No water supply for 3 days in Sector 5. Residents are having difficulties cooking and washing.")
                    .category(Grievance.Category.WATER_SUPPLY)
                    .severity(Grievance.Severity.HIGH)
                    .status(Grievance.Status.IN_PROGRESS)
                    .assignedDepartment(Grievance.Department.WATER_DEPT)
                    .location("Sector 5, Ward 12")
                    .ward("Ward 12")
                    .slaDays(2)
                    .escalationLevel(0)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .dueDate(LocalDateTime.now().plusDays(1))
                    .build();
            grievanceRepository.save(waterSupplyGrievance);

            // Seed an overdue grievance to show SLA breach escalation capabilities
            Grievance overdueGrievance = Grievance.builder()
                    .id(UUID.fromString("f48b1111-2222-3333-4444-555566667777"))
                    .citizenId(UUID.fromString("49b92718-47e5-4ebc-ba3d-abfa7b2a60cc"))
                    .citizenName("Priya Sharma")
                    .title("Garbage Pileup in Lane 3")
                    .description("Garbage has not been collected for a week, causing foul smell, stray dog activity, and health risks.")
                    .category(Grievance.Category.WASTE_MANAGEMENT)
                    .severity(Grievance.Severity.CRITICAL)
                    .status(Grievance.Status.SUBMITTED)
                    .assignedDepartment(Grievance.Department.SANITATION)
                    .location("Lane 3, Ward 8")
                    .ward("Ward 8")
                    .slaDays(1)
                    .escalationLevel(0)
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .dueDate(LocalDateTime.now().minusDays(2)) // Overdue!
                    .build();
            grievanceRepository.save(overdueGrievance);

            // Seed Streetlight Failure on MG Road (Vikram Malhotra)
            Grievance streetlightGrievance = Grievance.builder()
                    .id(UUID.fromString("6a117b4c-9f20-4e4b-9d41-e940f8fa8472"))
                    .citizenId(UUID.fromString("e3d748f2-824f-4d37-8ff0-d128cbda9e11"))
                    .citizenName("Vikram Malhotra")
                    .title("Broken Streetlights on Mahatma Gandhi Road")
                    .description("Entire stretch of Mahatma Gandhi Road near Central Park has broken streetlights. It is pitch dark at night, making it unsafe for women and children.")
                    .category(Grievance.Category.ELECTRICITY)
                    .severity(Grievance.Severity.MEDIUM)
                    .status(Grievance.Status.SUBMITTED)
                    .assignedDepartment(Grievance.Department.ELECTRICITY_DEPT)
                    .location("Mahatma Gandhi Road, Ward 4")
                    .ward("Ward 4")
                    .slaDays(3)
                    .escalationLevel(0)
                    .createdAt(LocalDateTime.now().minusHours(12))
                    .dueDate(LocalDateTime.now().plusDays(2))
                    .build();
            grievanceRepository.save(streetlightGrievance);

            // Seed Potholes & Road Cracks on Main Market Road (Sunita Rao)
            Grievance roadGrievance = Grievance.builder()
                    .id(UUID.fromString("6a117b4c-9f20-4e4b-9d41-e940f8fa8473"))
                    .citizenId(UUID.fromString("fa7a8b9c-10d9-4fa2-8b3d-cbda9e112233"))
                    .citizenName("Sunita Rao")
                    .title("Dangerous Potholes on Main Market Road")
                    .description("Deep potholes have formed in the middle of Main Market Road. Two motorcyclists slipped yesterday trying to avoid them. Needs immediate tar patching.")
                    .category(Grievance.Category.ROAD_MAINTENANCE)
                    .severity(Grievance.Severity.HIGH)
                    .status(Grievance.Status.IN_PROGRESS)
                    .assignedDepartment(Grievance.Department.PUBLIC_WORKS)
                    .location("Main Market Road, Ward 9")
                    .ward("Ward 9")
                    .slaDays(5)
                    .escalationLevel(0)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .dueDate(LocalDateTime.now().plusDays(3))
                    .build();
            grievanceRepository.save(roadGrievance);

            // Seed Sewage Overflow near Public School (Amit Patel)
            Grievance sewageGrievance = Grievance.builder()
                    .id(UUID.fromString("6a117b4c-9f20-4e4b-9d41-e940f8fa8474"))
                    .citizenId(UUID.fromString("bc8a7c6b-1234-4567-89ab-cdef01234567"))
                    .citizenName("Amit Patel")
                    .title("Drainage & Sewage Overflow near Public School")
                    .description("Black sewage water is overflowing onto the street right outside the entry gate of Little Hearts Public School. Children have to step through dirty water to enter. Highly unhygienic.")
                    .category(Grievance.Category.WASTE_MANAGEMENT)
                    .severity(Grievance.Severity.CRITICAL)
                    .status(Grievance.Status.SUBMITTED)
                    .assignedDepartment(Grievance.Department.SANITATION)
                    .location("Public School Road, Ward 15")
                    .ward("Ward 15")
                    .slaDays(1)
                    .escalationLevel(0)
                    .createdAt(LocalDateTime.now().minusHours(2))
                    .dueDate(LocalDateTime.now().plusHours(22))
                    .build();
            grievanceRepository.save(sewageGrievance);

            log.info("Grievance database seeding complete.");
        } else {
            log.info("Grievance database already contains data. Skipping seeding.");
        }
    }
}
