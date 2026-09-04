package com.civicpulse.service.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "certificate_number", unique = true, nullable = false)
    private String certificateNumber;

    @Column(name = "citizen_name", nullable = false)
    private String citizenName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Application.Type type;

    @Column(name = "issued_date", nullable = false)
    private LocalDateTime issuedDate;

    @Column(name = "digital_signature", nullable = false)
    private String digitalSignature;

    @Column(name = "download_count", nullable = false)
    private int downloadCount;

    public Certificate() {}

    public Certificate(UUID id, UUID applicationId, String certificateNumber, String citizenName, Application.Type type, LocalDateTime issuedDate, String digitalSignature, int downloadCount) {
        this.id = id;
        this.applicationId = applicationId;
        this.certificateNumber = certificateNumber;
        this.citizenName = citizenName;
        this.type = type;
        this.issuedDate = issuedDate;
        this.digitalSignature = digitalSignature;
        this.downloadCount = downloadCount;
    }

    @PrePersist
    protected void onCreate() {
        if (issuedDate == null) {
            issuedDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }

    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public Application.Type getType() { return type; }
    public void setType(Application.Type type) { this.type = type; }

    public LocalDateTime getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDateTime issuedDate) { this.issuedDate = issuedDate; }

    public String getDigitalSignature() { return digitalSignature; }
    public void setDigitalSignature(String digitalSignature) { this.digitalSignature = digitalSignature; }

    public int getDownloadCount() { return downloadCount; }
    public void setDownloadCount(int downloadCount) { this.downloadCount = downloadCount; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID applicationId;
        private String certificateNumber;
        private String citizenName;
        private Application.Type type;
        private LocalDateTime issuedDate;
        private String digitalSignature;
        private int downloadCount;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder applicationId(UUID applicationId) { this.applicationId = applicationId; return this; }
        public Builder certificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; return this; }
        public Builder citizenName(String citizenName) { this.citizenName = citizenName; return this; }
        public Builder type(Application.Type type) { this.type = type; return this; }
        public Builder issuedDate(LocalDateTime issuedDate) { this.issuedDate = issuedDate; return this; }
        public Builder digitalSignature(String digitalSignature) { this.digitalSignature = digitalSignature; return this; }
        public Builder downloadCount(int downloadCount) { this.downloadCount = downloadCount; return this; }

        public Certificate build() {
            return new Certificate(id, applicationId, certificateNumber, citizenName, type, issuedDate, digitalSignature, downloadCount);
        }
    }
}
