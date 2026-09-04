package com.civicpulse.service.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "permits")
public class Permit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "permit_number", unique = true, nullable = false)
    private String permitNumber;

    @Column(name = "citizen_name", nullable = false)
    private String citizenName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Application.Type type;

    @Column(name = "issued_date", nullable = false)
    private LocalDateTime issuedDate;

    @Column(name = "validity_date", nullable = false)
    private LocalDateTime validityDate;

    @Column(nullable = false)
    private String status; // ACTIVE, EXPIRED, SUSPENDED

    public Permit() {}

    public Permit(UUID id, UUID applicationId, String permitNumber, String citizenName, Application.Type type, LocalDateTime issuedDate, LocalDateTime validityDate, String status) {
        this.id = id;
        this.applicationId = applicationId;
        this.permitNumber = permitNumber;
        this.citizenName = citizenName;
        this.type = type;
        this.issuedDate = issuedDate;
        this.validityDate = validityDate;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (issuedDate == null) {
            issuedDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }

    public String getPermitNumber() { return permitNumber; }
    public void setPermitNumber(String permitNumber) { this.permitNumber = permitNumber; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public Application.Type getType() { return type; }
    public void setType(Application.Type type) { this.type = type; }

    public LocalDateTime getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDateTime issuedDate) { this.issuedDate = issuedDate; }

    public LocalDateTime getValidityDate() { return validityDate; }
    public void setValidityDate(LocalDateTime validityDate) { this.validityDate = validityDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID applicationId;
        private String permitNumber;
        private String citizenName;
        private Application.Type type;
        private LocalDateTime issuedDate;
        private LocalDateTime validityDate;
        private String status;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder applicationId(UUID applicationId) { this.applicationId = applicationId; return this; }
        public Builder permitNumber(String permitNumber) { this.permitNumber = permitNumber; return this; }
        public Builder citizenName(String citizenName) { this.citizenName = citizenName; return this; }
        public Builder type(Application.Type type) { this.type = type; return this; }
        public Builder issuedDate(LocalDateTime issuedDate) { this.issuedDate = issuedDate; return this; }
        public Builder validityDate(LocalDateTime validityDate) { this.validityDate = validityDate; return this; }
        public Builder status(String status) { this.status = status; return this; }

        public Permit build() {
            return new Permit(id, applicationId, permitNumber, citizenName, type, issuedDate, validityDate, status);
        }
    }
}
