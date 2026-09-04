package com.civicpulse.grievance.domain;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "grievances")
public class Grievance implements Persistable<UUID> {

    public enum Category {
        WATER_SUPPLY,
        ELECTRICITY,
        ROAD_MAINTENANCE,
        WASTE_MANAGEMENT,
        PUBLIC_HEALTH,
        OTHERS
    }

    public enum Severity {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum Status {
        SUBMITTED,
        IN_PROGRESS,
        RESOLVED,
        CLOSED
    }

    public enum Department {
        UNASSIGNED,
        WATER_DEPT,
        ELECTRICITY_DEPT,
        PUBLIC_WORKS,
        SANITATION,
        HEALTH_DEPT,
        ADMIN_DEPT
    }

    @Id
    private UUID id;

    @Transient
    private boolean isNewEntity = true;

    @Column(name = "citizen_id", nullable = false)
    private UUID citizenId;

    @Column(name = "citizen_name", nullable = false)
    private String citizenName;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "assigned_department", nullable = false)
    private Department assignedDepartment;

    private String location;
    private String ward;

    @Column(name = "sla_days")
    private int slaDays;

    @Column(name = "escalation_level")
    private int escalationLevel;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    public Grievance() {}

    public Grievance(UUID id, UUID citizenId, String citizenName, String title, String description, Category category, Severity severity, Status status, Department assignedDepartment, String location, String ward, int slaDays, int escalationLevel, LocalDateTime createdAt, LocalDateTime dueDate, LocalDateTime resolvedAt, String resolutionNotes) {
        this.id = id;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.title = title;
        this.description = description;
        this.category = category;
        this.severity = severity;
        this.status = status;
        this.assignedDepartment = assignedDepartment;
        this.location = location;
        this.ward = ward;
        this.slaDays = slaDays;
        this.escalationLevel = escalationLevel;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.resolvedAt = resolvedAt;
        this.resolutionNotes = resolutionNotes;
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
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.SUBMITTED;
        }
        if (assignedDepartment == null) {
            assignedDepartment = Department.UNASSIGNED;
        }
        if (dueDate == null && slaDays > 0) {
            dueDate = createdAt.plusDays(slaDays);
        }
        this.isNewEntity = false;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCitizenId() { return citizenId; }
    public void setCitizenId(UUID citizenId) { this.citizenId = citizenId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Department getAssignedDepartment() { return assignedDepartment; }
    public void setAssignedDepartment(Department assignedDepartment) { this.assignedDepartment = assignedDepartment; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public int getSlaDays() { return slaDays; }
    public void setSlaDays(int slaDays) { this.slaDays = slaDays; }

    public int getEscalationLevel() { return escalationLevel; }
    public void setEscalationLevel(int escalationLevel) { this.escalationLevel = escalationLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID citizenId;
        private String citizenName;
        private String title;
        private String description;
        private Category category;
        private Severity severity;
        private Status status;
        private Department assignedDepartment;
        private String location;
        private String ward;
        private int slaDays;
        private int escalationLevel;
        private LocalDateTime createdAt;
        private LocalDateTime dueDate;
        private LocalDateTime resolvedAt;
        private String resolutionNotes;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder citizenId(UUID citizenId) { this.citizenId = citizenId; return this; }
        public Builder citizenName(String citizenName) { this.citizenName = citizenName; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder category(Category category) { this.category = category; return this; }
        public Builder severity(Severity severity) { this.severity = severity; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder assignedDepartment(Department assignedDepartment) { this.assignedDepartment = assignedDepartment; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder ward(String ward) { this.ward = ward; return this; }
        public Builder slaDays(int slaDays) { this.slaDays = slaDays; return this; }
        public Builder escalationLevel(int escalationLevel) { this.escalationLevel = escalationLevel; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public Builder resolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; return this; }
        public Builder resolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; return this; }

        public Grievance build() {
            return new Grievance(id, citizenId, citizenName, title, description, category, severity, status, assignedDepartment, location, ward, slaDays, escalationLevel, createdAt, dueDate, resolvedAt, resolutionNotes);
        }
    }
}
