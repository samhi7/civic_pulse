package com.civicpulse.welfare.domain;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "welfare_schemes")
public class WelfareScheme implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isNewEntity = true;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "allocated_amount", nullable = false)
    private BigDecimal allocatedAmount;

    @Column(name = "disbursed_amount", nullable = false)
    private BigDecimal disbursedAmount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean active;

    public WelfareScheme() {}

    public WelfareScheme(UUID id, String name, BigDecimal allocatedAmount, BigDecimal disbursedAmount, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.allocatedAmount = allocatedAmount;
        this.disbursedAmount = disbursedAmount;
        this.description = description;
        this.active = active;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    @PostLoad
    protected void markNotNew() {
        this.isNewEntity = false;
    }

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (allocatedAmount == null) allocatedAmount = BigDecimal.ZERO;
        if (disbursedAmount == null) disbursedAmount = BigDecimal.ZERO;
        active = true;
        this.isNewEntity = false;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }

    public BigDecimal getDisbursedAmount() { return disbursedAmount; }
    public void setDisbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private BigDecimal allocatedAmount;
        private BigDecimal disbursedAmount;
        private String description;
        private boolean active;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder allocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; return this; }
        public Builder disbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public WelfareScheme build() {
            return new WelfareScheme(id, name, allocatedAmount, disbursedAmount, description, active);
        }
    }
}
