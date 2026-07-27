package com.example.civicpulse.service;

import com.example.civicpulse.model.Complaint;
import com.example.civicpulse.model.User;
import com.example.civicpulse.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
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
        
        // Save first to obtain the auto-incremented database ID
        Complaint saved = complaintRepository.save(complaint);
        
        // Generate a clean tracking number (e.g. CP-1001, CP-1002)
        saved.setComplaintId("CP-" + (1000 + saved.getId()));
        
        return complaintRepository.save(saved);
    }

    // 5. Update complaint details (for Admin update screen)
    public void updateComplaint(Long id, String status, String department, String adminRemarks) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            complaint.setStatus(status);
            complaint.setDepartment(department);
            complaint.setAdminRemarks(adminRemarks);
            complaintRepository.save(complaint);
        });
    }

    // 6. Support / Upvote a complaint (limits to one support per user via Set)
    public void supportComplaint(Long id, User user) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            complaint.getSupportingUsers().add(user);
            complaintRepository.save(complaint);
        });
    }

    // 7. Metric aggregations for user dashboard
    public long getTotalCount(User user) {
        return complaintRepository.countByUser(user);
    }

    public long getPendingCount(User user) {
        return complaintRepository.countByUserAndStatus(user, "Pending") 
             + complaintRepository.countByUserAndStatus(user, "In Progress");
    }

    public long getResolvedCount(User user) {
        return complaintRepository.countByUserAndStatus(user, "Resolved");
    }

    // 8. Global metric aggregations for admin dashboard
    public long getGlobalTotalCount() {
        return complaintRepository.count();
    }

    public long getGlobalPendingCount() {
        return complaintRepository.findAll().stream()
            .filter(c -> !"Resolved".equals(c.getStatus()))
            .count();
    }

    public long getGlobalResolvedCount() {
        return complaintRepository.findAll().stream()
            .filter(c -> "Resolved".equals(c.getStatus()))
            .count();
    }
}
