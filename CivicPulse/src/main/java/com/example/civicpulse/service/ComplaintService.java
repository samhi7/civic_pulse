package com.example.civicpulse.service;

import com.example.civicpulse.model.AuditLog;
import com.example.civicpulse.model.Complaint;
import com.example.civicpulse.model.ComplaintComment;
import com.example.civicpulse.model.User;
import com.example.civicpulse.repository.AuditLogRepository;
import com.example.civicpulse.repository.ComplaintCommentRepository;
import com.example.civicpulse.repository.ComplaintRepository;
import com.example.civicpulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintCommentRepository commentRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository,
            ComplaintCommentRepository commentRepository,
            AuditLogRepository auditLogRepository,
            UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.commentRepository = commentRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    // 1. Get all complaints for a specific user
    public List<Complaint> getComplaintsByUser(User user) {
        return complaintRepository.findByUserOrderByIdDesc(user);
    }

    // 2. Get all complaints globally (for Admin & Community Board)
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAllByOrderByIdDesc();
    }

    // 3. Get complaint by ID
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElse(null);
    }

    // 4. File a new complaint
    public Complaint fileComplaint(Complaint complaint, User user) {
        complaint.setUser(user);
        complaint.setStatus("Pending");
        complaint.setDepartment("Unassigned");
        complaint.setAdminRemarks("Awaiting municipal review.");
        complaint.setDateSubmitted(LocalDate.now());
        complaint.setDateUpdated(LocalDate.now());

        // Save first to obtain the auto-incremented database ID
        Complaint saved = complaintRepository.save(complaint);

        // Generate a clean tracking number (e.g. CP-1001, CP-1002)
        saved.setComplaintId("CP-" + (1000 + saved.getId()));

        // Log the action
        logAction("Report Filed",
                "Complaint " + saved.getComplaintId() + " successfully filed by citizen " + user.getFullName(), user);

        return complaintRepository.save(saved);
    }

    // 5. Update complaint details (for Admin update screen)
    public void updateComplaint(Long id, String status, String department, String adminRemarks) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            String oldStatus = complaint.getStatus();
            complaint.setStatus(status);
            complaint.setDepartment(department);
            complaint.setAdminRemarks(adminRemarks);
            complaint.setDateUpdated(LocalDate.now());
            complaintRepository.save(complaint);

            logAction("Status Modified", "Complaint " + complaint.getComplaintId() + " changed from " + oldStatus
                    + " to " + status + " with department: " + department, null);
        });
    }

    // 6. Assign officer and department
    public void assignOfficer(Long id, Long officerId, String department, String remarks, User admin) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            User officer = userRepository.findById(officerId).orElse(null);
            complaint.setAssignedOfficer(officer);
            complaint.setDepartment(department);
            complaint.setStatus("Assigned");
            complaint.setAdminRemarks(remarks);
            complaint.setDateUpdated(LocalDate.now());
            complaintRepository.save(complaint);

            String officerName = (officer != null) ? officer.getFullName() : "None";
            logAction("Officer Assigned", "Complaint " + complaint.getComplaintId() + " assigned to officer "
                    + officerName + " (" + department + ")", admin);
        });
    }

    // 7. Update officer progress (field reporting)
    public void updateOfficerProgress(Long id, String status, String remarks, String afterPhotoUrl, User officer) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            complaint.setStatus(status);
            complaint.setOfficerRemarks(remarks);
            if (afterPhotoUrl != null && !afterPhotoUrl.isEmpty()) {
                complaint.setAfterPhotoUrl(afterPhotoUrl);
            }
            complaint.setDateUpdated(LocalDate.now());
            complaintRepository.save(complaint);

            logAction("Progress Updated",
                    "Officer " + officer.getFullName() + " updated " + complaint.getComplaintId() + " to " + status,
                    officer);
        });
    }

    // 8. Submit citizen feedback rating
    public void submitRating(Long id, Integer rating, String feedback) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            complaint.setCitizenRating(rating);
            complaint.setCitizenFeedback(feedback);
            complaint.setDateUpdated(LocalDate.now());
            complaintRepository.save(complaint);

            logAction("Feedback Submitted",
                    "Citizen submitted a " + rating + "-star rating for " + complaint.getComplaintId(),
                    complaint.getUser());
        });
    }

    // 9. Support / Upvote a complaint
    public void supportComplaint(Long id, User user) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            complaint.getSupportingUsers().add(user);
            complaintRepository.save(complaint);
            logAction("Upvote Registered",
                    "User " + user.getFullName() + " supported issue " + complaint.getComplaintId(), user);
        });
    }

    // 10. Add discussion comment
    public void addComment(Long id, String text, User user) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            ComplaintComment comment = new ComplaintComment(text, user, complaint);
            commentRepository.save(comment);
            logAction("Comment Added",
                    "User " + user.getFullName() + " added a comment to " + complaint.getComplaintId(), user);
        });
    }

    // 11. Get complaints by assigned officer
    public List<Complaint> getComplaintsByOfficer(User officer) {
        return complaintRepository.findByAssignedOfficerOrderByIdDesc(officer);
    }

    // 12. Metric aggregations for user dashboard
    public long getTotalCount(User user) {
        return complaintRepository.countByUser(user);
    }

    public long getPendingCount(User user) {
        return complaintRepository.countByUserAndStatus(user, "Pending")
                + complaintRepository.countByUserAndStatus(user, "Verified");
    }

    public long getResolvedCount(User user) {
        return complaintRepository.countByUserAndStatus(user, "Completed");
    }

    // 13. Global metrics
    public long getGlobalTotalCount() {
        return complaintRepository.count();
    }

    public long getGlobalPendingCount() {
        return complaintRepository.findAll().stream()
                .filter(c -> !"Completed".equals(c.getStatus()))
                .count();
    }

    public long getGlobalResolvedCount() {
        return complaintRepository.findAll().stream()
                .filter(c -> "Completed".equals(c.getStatus()))
                .count();
    }

    // 14. Auditing
    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }

    public void logAction(String action, String details, User user) {
        AuditLog log = new AuditLog(action, details, user);
        auditLogRepository.save(log);
    }
}
