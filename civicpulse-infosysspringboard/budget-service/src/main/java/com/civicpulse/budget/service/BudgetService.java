package com.civicpulse.budget.service;

import com.civicpulse.budget.domain.DepartmentBudget;
import com.civicpulse.budget.domain.Expense;
import com.civicpulse.budget.repository.DepartmentBudgetRepository;
import com.civicpulse.budget.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BudgetService {

    private static final Logger log = LoggerFactory.getLogger(BudgetService.class);

    private final DepartmentBudgetRepository departmentBudgetRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(DepartmentBudgetRepository departmentBudgetRepository, ExpenseRepository expenseRepository) {
        this.departmentBudgetRepository = departmentBudgetRepository;
        this.expenseRepository = expenseRepository;
    }

    public DepartmentBudget allocateBudget(DepartmentBudget budget) {
        Optional<DepartmentBudget> existing = departmentBudgetRepository.findByDepartmentNameIgnoreCase(budget.getDepartmentName());
        if (existing.isPresent()) {
            DepartmentBudget db = existing.get();
            db.setAllocatedAmount(budget.getAllocatedAmount());
            db.setYear(budget.getYear());
            return departmentBudgetRepository.save(db);
        }
        return departmentBudgetRepository.save(budget);
    }

    public DepartmentBudget recordExpense(UUID budgetId, String description, BigDecimal amount) {
        DepartmentBudget budget = departmentBudgetRepository.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("Department budget not found."));

        // Record expense transaction
        Expense expense = Expense.builder()
                .departmentBudgetId(budgetId)
                .description(description)
                .amount(amount)
                .build();
        expenseRepository.save(expense);

        // Update disbursed amount
        budget.setDisbursedAmount(budget.getDisbursedAmount().add(amount));
        
        // Log warning if budget utilization exceeds allocated amount
        if (budget.getDisbursedAmount().compareTo(budget.getAllocatedAmount()) > 0) {
            log.warn("BUDGET BREACH: Department {} has exceeded its allocated budget! Spent: {}, Allocated: {}",
                    budget.getDepartmentName(), budget.getDisbursedAmount(), budget.getAllocatedAmount());
        }

        return departmentBudgetRepository.save(budget);
    }

    public DepartmentBudget recordExternalExpense(String departmentName, BigDecimal amount, String description) {
        DepartmentBudget budget = departmentBudgetRepository.findByDepartmentNameIgnoreCase(departmentName)
                .orElseThrow(() -> new IllegalArgumentException("Department budget for " + departmentName + " not found."));

        Expense expense = Expense.builder()
                .departmentBudgetId(budget.getId())
                .description(description)
                .amount(amount)
                .build();
        expenseRepository.save(expense);

        budget.setDisbursedAmount(budget.getDisbursedAmount().add(amount));
        log.info("Recorded external expense: '{}' of amount {} under department {}. New disbursed total: {}",
                description, amount, departmentName, budget.getDisbursedAmount());

        return departmentBudgetRepository.save(budget);
    }

    public List<DepartmentBudget> getAllBudgets() {
        return departmentBudgetRepository.findAll();
    }

    public Optional<DepartmentBudget> getBudgetById(UUID id) {
        return departmentBudgetRepository.findById(id);
    }
}
