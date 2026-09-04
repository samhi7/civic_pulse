package com.civicpulse.budget;

import com.civicpulse.budget.domain.DepartmentBudget;
import com.civicpulse.budget.domain.Expense;
import com.civicpulse.budget.repository.DepartmentBudgetRepository;
import com.civicpulse.budget.repository.ExpenseRepository;
import com.civicpulse.budget.service.BudgetService;
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

class BudgetServiceTest {

    @Mock
    private DepartmentBudgetRepository departmentBudgetRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allocateBudget_SavesOrUpdates() {
        DepartmentBudget budget = DepartmentBudget.builder()
                .departmentName("Water Dept")
                .allocatedAmount(new BigDecimal("8000000.00"))
                .year(2026)
                .build();

        when(departmentBudgetRepository.findByDepartmentNameIgnoreCase("Water Dept")).thenReturn(Optional.empty());
        when(departmentBudgetRepository.save(any(DepartmentBudget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentBudget result = budgetService.allocateBudget(budget);

        assertNotNull(result);
        assertEquals("Water Dept", result.getDepartmentName());
        verify(departmentBudgetRepository, times(1)).save(budget);
    }

    @Test
    void recordExpense_IncrementsSpentAndSavesExpense() {
        UUID budgetId = UUID.randomUUID();
        DepartmentBudget budget = DepartmentBudget.builder()
                .id(budgetId)
                .departmentName("Housing")
                .allocatedAmount(new BigDecimal("2400000.00"))
                .disbursedAmount(new BigDecimal("2100000.00"))
                .year(2026)
                .build();

        when(departmentBudgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        when(departmentBudgetRepository.save(any(DepartmentBudget.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentBudget result = budgetService.recordExpense(budgetId, "Disbursement", new BigDecimal("2400.00"));

        assertNotNull(result);
        assertEquals(new BigDecimal("2102400.00"), result.getDisbursedAmount());
        verify(expenseRepository, times(1)).save(any(Expense.class));
        verify(departmentBudgetRepository, times(1)).save(budget);
    }

    @Test
    void recordExternalExpense_LocatesByNameAndUpdates() {
        DepartmentBudget budget = DepartmentBudget.builder()
                .id(UUID.randomUUID())
                .departmentName("Housing")
                .allocatedAmount(new BigDecimal("2400000.00"))
                .disbursedAmount(new BigDecimal("2100000.00"))
                .year(2026)
                .build();

        when(departmentBudgetRepository.findByDepartmentNameIgnoreCase("Housing")).thenReturn(Optional.of(budget));
        when(departmentBudgetRepository.save(any(DepartmentBudget.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentBudget result = budgetService.recordExternalExpense("Housing", new BigDecimal("2400.00"), "Disbursement to Ramesh Kumar");

        assertNotNull(result);
        assertEquals(new BigDecimal("2102400.00"), result.getDisbursedAmount());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
}
