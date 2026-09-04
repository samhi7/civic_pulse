package com.civicpulse.budget.config;

import com.civicpulse.budget.domain.DepartmentBudget;
import com.civicpulse.budget.repository.DepartmentBudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final DepartmentBudgetRepository departmentBudgetRepository;

    public DataInitializer(DepartmentBudgetRepository departmentBudgetRepository) {
        this.departmentBudgetRepository = departmentBudgetRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding database with default department budgets...");

        if (departmentBudgetRepository.count() == 0) {
            // Housing (corresponds to PM Awas Yojana Welfare Scheme)
            DepartmentBudget housingBudget = DepartmentBudget.builder()
                    .departmentName("Housing")
                    .allocatedAmount(new BigDecimal("2400000.00")) // $2.4M
                    .disbursedAmount(new BigDecimal("2100000.00")) // $2.1M
                    .year(2026)
                    .build();
            departmentBudgetRepository.save(housingBudget);

            // Water Dept
            DepartmentBudget waterBudget = DepartmentBudget.builder()
                    .departmentName("Water Dept")
                    .allocatedAmount(new BigDecimal("8000000.00")) // $8M
                    .disbursedAmount(new BigDecimal("6800000.00")) // $6.8M
                    .year(2026)
                    .build();
            departmentBudgetRepository.save(waterBudget);

            // Health Dept
            DepartmentBudget healthBudget = DepartmentBudget.builder()
                    .departmentName("Health Dept")
                    .allocatedAmount(new BigDecimal("5000000.00")) // $5M
                    .disbursedAmount(new BigDecimal("4500000.00")) // $4.5M
                    .year(2026)
                    .build();
            departmentBudgetRepository.save(healthBudget);

            // Education Dept
            DepartmentBudget educationBudget = DepartmentBudget.builder()
                    .departmentName("Education Dept")
                    .allocatedAmount(new BigDecimal("4000000.00")) // $4M
                    .disbursedAmount(new BigDecimal("3100000.00")) // $3.1M
                    .year(2026)
                    .build();
            departmentBudgetRepository.save(educationBudget);

            log.info("Department budgets database seeding complete.");
        } else {
            log.info("Department budgets already seeded. Skipping.");
        }
    }
}
