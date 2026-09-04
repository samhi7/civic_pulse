package com.civicpulse.budget.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "department_budgets")
public class DepartmentBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "allocated_amount", nullable = false)
    private BigDecimal allocatedAmount;

    @Column(name = "disbursed_amount", nullable = false)
    private BigDecimal disbursedAmount;

    @Column(name = "budget_year", nullable = false)
    private int year;

    public DepartmentBudget() {}

    public DepartmentBudget(UUID id, String departmentName, BigDecimal allocatedAmount, BigDecimal disbursedAmount, int year) {
        this.id = id;
        this.departmentName = departmentName;
        this.allocatedAmount = allocatedAmount;
        this.disbursedAmount = disbursedAmount;
        this.year = year;
    }

    @PrePersist
    protected void onCreate() {
        if (allocatedAmount == null) allocatedAmount = BigDecimal.ZERO;
        if (disbursedAmount == null) disbursedAmount = BigDecimal.ZERO;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }

    public BigDecimal getDisbursedAmount() { return disbursedAmount; }
    public void setDisbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String departmentName;
        private BigDecimal allocatedAmount;
        private BigDecimal disbursedAmount;
        private int year;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public Builder allocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; return this; }
        public Builder disbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; return this; }
        public Builder year(int year) { this.year = year; return this; }

        public DepartmentBudget build() {
            return new DepartmentBudget(id, departmentName, allocatedAmount, disbursedAmount, year);
        }
    }
}
