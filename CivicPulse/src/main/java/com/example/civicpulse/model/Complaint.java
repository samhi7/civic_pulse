package com.example.civicpulse.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String complaintId; // e.g. CP-1001
    private String category;
    private String status;      // Pending, In Progress, Resolved
    private String title;
    private String description;
    private String location;

    private String department = "Unassigned";
    private String adminRemarks = "";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "complaint_supports",
        joinColumns = @JoinColumn(name = "complaint_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> supportingUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Default constructor
    public Complaint() {
    }

    // Parameterized constructor
    public Complaint(String complaintId, String category, String status, String title, String description, String location, User user) {
        this.complaintId = complaintId;
        this.category = category;
        this.status = status;
        this.title = title;
        this.description = description;
        this.location = location;
        this.user = user;
    }

    // Parameterized constructor with additional features (useful for seeding)
    public Complaint(String complaintId, String category, String status, String title, String description, String location, String department, String adminRemarks, User user) {
        this.complaintId = complaintId;
        this.category = category;
        this.status = status;
        this.title = title;
        this.description = description;
        this.location = location;
        this.department = department;
        this.adminRemarks = adminRemarks;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    public Set<User> getSupportingUsers() {
        return supportingUsers;
    }

    public void setSupportingUsers(Set<User> supportingUsers) {
        this.supportingUsers = supportingUsers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Computed upvote/support count
    public int getSupportCount() {
        return this.supportingUsers != null ? this.supportingUsers.size() : 0;
    }

    // Helper method to check if a user has already supported this issue
    public boolean isSupportedByUser(String email) {
        if (this.supportingUsers == null || email == null) {
            return false;
        }
        return this.supportingUsers.stream()
            .anyMatch(u -> email.equalsIgnoreCase(u.getEmail()));
    }
}
