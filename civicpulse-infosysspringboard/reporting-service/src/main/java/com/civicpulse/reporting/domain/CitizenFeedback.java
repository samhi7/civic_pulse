package com.civicpulse.reporting.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "citizen_feedback")
public class CitizenFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "citizen_id", nullable = false)
    private UUID citizenId;

    @Column(name = "citizen_name", nullable = false)
    private String citizenName;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int rating; // 1 to 5 stars

    @Column(length = 1000)
    private String comments;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CitizenFeedback() {}

    public CitizenFeedback(UUID id, UUID citizenId, String citizenName, String category, int rating, String comments, LocalDateTime createdAt) {
        this.id = id;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.category = category;
        this.rating = rating;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCitizenId() { return citizenId; }
    public void setCitizenId(UUID citizenId) { this.citizenId = citizenId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID citizenId;
        private String citizenName;
        private String category;
        private int rating;
        private String comments;
        private LocalDateTime createdAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder citizenId(UUID citizenId) { this.citizenId = citizenId; return this; }
        public Builder citizenName(String citizenName) { this.citizenName = citizenName; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder rating(int rating) { this.rating = rating; return this; }
        public Builder comments(String comments) { this.comments = comments; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public CitizenFeedback build() {
            return new CitizenFeedback(id, citizenId, citizenName, category, rating, comments, createdAt);
        }
    }
}
