package com.civicpulse.budget.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "department_budget_id", nullable = false)
    private UUID departmentBudgetId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Expense() {}

    public Expense(UUID id, UUID departmentBudgetId, String description, BigDecimal amount, LocalDateTime createdAt) {
        this.id = id;
        this.departmentBudgetId = departmentBudgetId;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getDepartmentBudgetId() { return departmentBudgetId; }
    public void setDepartmentBudgetId(UUID departmentBudgetId) { this.departmentBudgetId = departmentBudgetId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID departmentBudgetId;
        private String description;
        private BigDecimal amount;
        private LocalDateTime createdAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder departmentBudgetId(UUID departmentBudgetId) { this.departmentBudgetId = departmentBudgetId; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Expense build() {
            return new Expense(id, departmentBudgetId, description, amount, createdAt);
        }
    }
}
