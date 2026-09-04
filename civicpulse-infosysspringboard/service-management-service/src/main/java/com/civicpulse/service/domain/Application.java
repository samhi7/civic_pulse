package com.civicpulse.service.domain;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class Application implements Persistable<UUID> {

    public enum Type {
        BIRTH_CERTIFICATE,
        DEATH_CERTIFICATE,
        INCOME_CERTIFICATE,
        RESIDENCE_CERTIFICATE,
        TRADE_LICENSE
    }

    public enum Status {
        SUBMITTED,
        DOCUMENT_VERIFIED,
        APPROVED,
        REJECTED
    }

    @Id
    private UUID id;

    @Transient
    private boolean isNewEntity = true;

    @Column(name = "application_number", unique = true, nullable = false)
    private String applicationNumber;

    @Column(name = "citizen_id", nullable = false)
    private UUID citizenId;

    @Column(name = "citizen_name", nullable = false)
    private String citizenName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String metadata; // e.g. "Child: Aarav, DOB: 15-May-2026"

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    public Application() {}

    public Application(UUID id, String applicationNumber, UUID citizenId, String citizenName, Type type, Status status, String metadata, LocalDateTime appliedDate, boolean isVerified) {
        this.id = id;
        this.applicationNumber = applicationNumber;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.type = type;
        this.status = status;
        this.metadata = metadata;
        this.appliedDate = appliedDate;
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
        if (appliedDate == null) {
            appliedDate = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.SUBMITTED;
        }
        this.isNewEntity = false;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }

    public UUID getCitizenId() { return citizenId; }
    public void setCitizenId(UUID citizenId) { this.citizenId = citizenId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public LocalDateTime getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDateTime appliedDate) { this.appliedDate = appliedDate; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean isVerified) { this.isVerified = isVerified; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String applicationNumber;
        private UUID citizenId;
        private String citizenName;
        private Type type;
        private Status status;
        private String metadata;
        private LocalDateTime appliedDate;
        private boolean isVerified;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder applicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; return this; }
        public Builder citizenId(UUID citizenId) { this.citizenId = citizenId; return this; }
        public Builder citizenName(String citizenName) { this.citizenName = citizenName; return this; }
        public Builder type(Type type) { this.type = type; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder metadata(String metadata) { this.metadata = metadata; return this; }
        public Builder appliedDate(LocalDateTime appliedDate) { this.appliedDate = appliedDate; return this; }
        public Builder isVerified(boolean isVerified) { this.isVerified = isVerified; return this; }

        public Application build() {
            return new Application(id, applicationNumber, citizenId, citizenName, type, status, metadata, appliedDate, isVerified);
        }
    }
}
