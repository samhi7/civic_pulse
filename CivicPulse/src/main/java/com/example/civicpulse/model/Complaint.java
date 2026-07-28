package com.example.civicpulse.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String complaintId; // e.g. CP-1001
    private String category;
    private String status;      // Pending, Verified, Assigned, Inspection, Work Started, Completed
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private String location;
    private Double latitude;
    private Double longitude;
    private String evidenceUrl; // Comma-separated or legacy URL
    
    // Core uploaded media paths
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String video;

    private String department = "Unassigned";
    
    @Column(length = 1000)
    private String adminRemarks = "";
    
    @Column(length = 1000)
    private String officerRemarks = "";
    
    private String beforePhotoUrl;
    private String afterPhotoUrl;
    
    @Column(length = 1000)
    private String citizenFeedback = "";
    private Integer citizenRating; // 1-5 scale
    
    private LocalDate dateSubmitted;
    private LocalDate dateUpdated;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id")
    private User assignedOfficer;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComplaintComment> comments = new ArrayList<>();

    // Default constructor
    public Complaint() {
        this.dateSubmitted = LocalDate.now();
        this.dateUpdated = LocalDate.now();
    }

    // Parameterized constructor
    public Complaint(String complaintId, String category, String status, String title, String description, String location, User user) {
        this();
        this.complaintId = complaintId;
        this.category = category;
        this.status = status;
        this.title = title;
        this.description = description;
        this.location = location;
        this.user = user;
    }

    // Full constructor (for seeding)
    public Complaint(String complaintId, String category, String status, String title, String description, 
                     String location, String department, String adminRemarks, User user) {
        this(complaintId, category, status, title, description, location, user);
        this.department = department;
        this.adminRemarks = adminRemarks;
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
        this.dateUpdated = LocalDate.now();
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEvidenceUrl() {
        return evidenceUrl;
    }

    public void setEvidenceUrl(String evidenceUrl) {
        this.evidenceUrl = evidenceUrl;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public String getOfficerRemarks() {
        return officerRemarks;
    }

    public void setOfficerRemarks(String officerRemarks) {
        this.officerRemarks = officerRemarks;
    }

    public String getBeforePhotoUrl() {
        return beforePhotoUrl;
    }

    public void setBeforePhotoUrl(String beforePhotoUrl) {
        this.beforePhotoUrl = beforePhotoUrl;
    }

    public String getAfterPhotoUrl() {
        return afterPhotoUrl;
    }

    public void setAfterPhotoUrl(String afterPhotoUrl) {
        this.afterPhotoUrl = afterPhotoUrl;
    }

    public String getCitizenFeedback() {
        return citizenFeedback;
    }

    public void setCitizenFeedback(String citizenFeedback) {
        this.citizenFeedback = citizenFeedback;
    }

    public Integer getCitizenRating() {
        return citizenRating;
    }

    public void setCitizenRating(Integer citizenRating) {
        this.citizenRating = citizenRating;
    }

    public LocalDate getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
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

    public User getAssignedOfficer() {
        return assignedOfficer;
    }

    public void setAssignedOfficer(User assignedOfficer) {
        this.assignedOfficer = assignedOfficer;
    }

    public List<ComplaintComment> getComments() {
        return comments;
    }

    public void setComments(List<ComplaintComment> comments) {
        this.comments = comments;
    }

    public void addComment(ComplaintComment comment) {
        comments.add(comment);
        comment.setComplaint(this);
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
