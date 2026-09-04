package com.civicpulse.welfare.domain;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary implements Persistable<UUID> {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED,
        DISBURSED
    }

    @Id
    private UUID id;

    @Transient
    private boolean isNewEntity = true;

    @Column(name = "scheme_id", nullable = false)
    private UUID schemeId;

    @Column(name = "citizen_id", nullable = false)
    private UUID citizenId;

    @Column(name = "citizen_name", nullable = false)
    private String citizenName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "disbursed_amount", nullable = false)
    private BigDecimal disbursedAmount;

    @Column(name = "eligibility_criteria")
    private String eligibilityCriteria;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    public Beneficiary() {}

    public Beneficiary(UUID id, UUID schemeId, UUID citizenId, String citizenName, Status status, BigDecimal disbursedAmount, String eligibilityCriteria, boolean isVerified) {
        this.id = id;
        this.schemeId = schemeId;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.status = status;
        this.disbursedAmount = disbursedAmount;
        this.eligibilityCriteria = eligibilityCriteria;
        this.isVerified = isVerified;
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
        if (status == null) status = Status.PENDING;
        if (disbursedAmount == null) disbursedAmount = BigDecimal.ZERO;
        this.isNewEntity = false;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSchemeId() { return schemeId; }
    public void setSchemeId(UUID schemeId) { this.schemeId = schemeId; }

    public UUID getCitizenId() { return citizenId; }
    public void setCitizenId(UUID citizenId) { this.citizenId = citizenId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public BigDecimal getDisbursedAmount() { return disbursedAmount; }
    public void setDisbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; }

    public String getEligibilityCriteria() { return eligibilityCriteria; }
    public void setEligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean isVerified) { this.isVerified = isVerified; }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID schemeId;
        private UUID citizenId;
        private String citizenName;
        private Status status;
        private BigDecimal disbursedAmount;
        private String eligibilityCriteria;
        private boolean isVerified;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder schemeId(UUID schemeId) { this.schemeId = schemeId; return this; }
        public Builder citizenId(UUID citizenId) { this.citizenId = citizenId; return this; }
        public Builder citizenName(String citizenName) { this.citizenName = citizenName; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder disbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; return this; }
        public Builder eligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; return this; }
        public Builder isVerified(boolean isVerified) { this.isVerified = isVerified; return this; }

        public Beneficiary build() {
            return new Beneficiary(id, schemeId, citizenId, citizenName, status, disbursedAmount, eligibilityCriteria, isVerified);
        }
    }
}
