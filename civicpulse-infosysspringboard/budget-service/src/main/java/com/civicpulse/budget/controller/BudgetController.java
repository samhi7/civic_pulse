package com.civicpulse.budget.controller;

import com.civicpulse.budget.domain.DepartmentBudget;
import com.civicpulse.budget.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<DepartmentBudget> allocateBudget(@RequestBody DepartmentBudget budget) {
        DepartmentBudget created = budgetService.allocateBudget(budget);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentBudget>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

    @PostMapping("/{id}/expenses")
    public ResponseEntity<DepartmentBudget> recordExpense(
            @PathVariable UUID id,
            @RequestParam String description,
            @RequestParam BigDecimal amount) {
        try {
            DepartmentBudget updated = budgetService.recordExpense(id, description, amount);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/record-external-expense")
    public ResponseEntity<Void> recordExternalExpense(
            @RequestParam String department,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        try {
            budgetService.recordExternalExpense(department, amount, description);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
