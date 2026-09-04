package com.civicpulse.reporting.config;

import com.civicpulse.reporting.domain.CitizenFeedback;
import com.civicpulse.reporting.domain.ComplianceAudit;
import com.civicpulse.reporting.domain.DepartmentPerformance;
import com.civicpulse.reporting.repository.CitizenFeedbackRepository;
import com.civicpulse.reporting.repository.ComplianceAuditRepository;
import com.civicpulse.reporting.repository.DepartmentPerformanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final DepartmentPerformanceRepository departmentRepo;
    private final CitizenFeedbackRepository feedbackRepo;
    private final ComplianceAuditRepository auditRepo;

    public DataInitializer(DepartmentPerformanceRepository departmentRepo,
                           CitizenFeedbackRepository feedbackRepo,
                           ComplianceAuditRepository auditRepo) {
        this.departmentRepo = departmentRepo;
        this.feedbackRepo = feedbackRepo;
        this.auditRepo = auditRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding reporting service data...");

        if (departmentRepo.count() == 0) {
            // Seed department performance matching Milestone 4 specifications:
            // "Departments: Water 94% | Health 91% | Education 89%"
            departmentRepo.save(DepartmentPerformance.builder()
                    .departmentName("Water Dept")
                    .resolutionRate(94.0)
                    .slaCompliance(96.5)
                    .avgResponseHours(24.0)
                    .satisfactionScore(4.8)
                    .totalCases(4200)
                    .resolvedCases(3948)
                    .build());

            departmentRepo.save(DepartmentPerformance.builder()
                    .departmentName("Health Dept")
                    .resolutionRate(91.0)
                    .slaCompliance(93.0)
                    .avgResponseHours(28.5)
                    .satisfactionScore(4.7)
                    .totalCases(3100)
                    .resolvedCases(2821)
                    .build());

            departmentRepo.save(DepartmentPerformance.builder()
                    .departmentName("Education Dept")
                    .resolutionRate(89.0)
                    .slaCompliance(91.2)
                    .avgResponseHours(32.0)
                    .satisfactionScore(4.6)
                    .totalCases(2800)
                    .resolvedCases(2492)
                    .build());

            departmentRepo.save(DepartmentPerformance.builder()
                    .departmentName("Sanitation")
                    .resolutionRate(88.0)
                    .slaCompliance(90.0)
                    .avgResponseHours(18.0)
                    .satisfactionScore(4.5)
                    .totalCases(3900)
                    .resolvedCases(3432)
                    .build());

            departmentRepo.save(DepartmentPerformance.builder()
                    .departmentName("Public Works")
                    .resolutionRate(86.0)
                    .slaCompliance(88.5)
                    .avgResponseHours(42.0)
                    .satisfactionScore(4.4)
                    .totalCases(2500)
                    .resolvedCases(2150)
                    .build());

            log.info("Department performance benchmarks seeded.");
        }

        if (feedbackRepo.count() == 0) {
            feedbackRepo.save(CitizenFeedback.builder()
                    .citizenId(UUID.fromString("36a83693-39f5-47ec-a63e-dbfa7b2a60bb"))
                    .citizenName("Ramesh Kumar")
                    .category("Water Supply")
                    .rating(5)
                    .comments("Water pipeline grievance was reviewed promptly. Very satisfied with the quick response.")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build());

            feedbackRepo.save(CitizenFeedback.builder()
                    .citizenId(UUID.fromString("49b92718-47e5-4ebc-ba3d-abfa7b2a60cc"))
                    .citizenName("Priya Sharma")
                    .category("Certificates & Approvals")
                    .rating(5)
                    .comments("Birth certificate verification and digital signature issued in under 2 days. Seamless experience.")
                    .createdAt(LocalDateTime.now().minusHours(18))
                    .build());

            feedbackRepo.save(CitizenFeedback.builder()
                    .citizenId(UUID.fromString("e3d748f2-824f-4d37-8ff0-d128cbda9e11"))
                    .citizenName("Vikram Malhotra")
                    .category("Electricity & Streetlights")
                    .rating(4)
                    .comments("Streetlight complaint logged smoothly on portal. Work crew arrived next day.")
                    .createdAt(LocalDateTime.now().minusHours(6))
                    .build());

            log.info("Citizen feedback entries seeded.");
        }

        if (auditRepo.count() == 0) {
            auditRepo.save(ComplianceAudit.builder()
                    .action("ANNUAL_GOVERNANCE_AUDIT")
                    .performedBy("Chief Municipal Auditor")
                    .details("Annual service delivery, certificate signing, and welfare budget allocation audit passed.")
                    .status("PASSED")
                    .timestamp(LocalDateTime.now().minusDays(2))
                    .build());
        }
    }
}
