package com.example.civicpulse.repository;

import com.example.civicpulse.model.Complaint;
import com.example.civicpulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserOrderByIdDesc(User user);
    List<Complaint> findAllByOrderByIdDesc();
    List<Complaint> findByAssignedOfficerOrderByIdDesc(User officer);
    long countByUser(User user);
    long countByUserAndStatus(User user, String status);
}
