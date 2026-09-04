package com.civicpulse.citizen.domain;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "citizens")
public class Citizen implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isNewEntity = true;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "aadhar_number", unique = true, nullable = false)
    private String aadharNumber;

    @Column(nullable = false)
    private String ward;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    public Citizen() {}

    public Citizen(UUID id, String name, String email, String phone, String aadharNumber, String ward, LocalDateTime registeredAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.aadharNumber = aadharNumber;
        this.ward = ward;
        this.registeredAt = registeredAt;
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
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
        this.isNewEntity = false;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String email;
        private String phone;
        private String aadharNumber;
        private String ward;
        private LocalDateTime registeredAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public Builder aadharNumber(String aadharNumber) {
            this.aadharNumber = aadharNumber;
            return this;
        }
        public Builder ward(String ward) {
            this.ward = ward;
            return this;
        }
        public Builder registeredAt(LocalDateTime registeredAt) {
            this.registeredAt = registeredAt;
            return this;
        }
        public Citizen build() {
            return new Citizen(id, name, email, phone, aadharNumber, ward, registeredAt);
        }
    }
}
