package com.civicpulse.reporting.repository;

import com.civicpulse.reporting.domain.CitizenFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CitizenFeedbackRepository extends JpaRepository<CitizenFeedback, UUID> {
    List<CitizenFeedback> findByCitizenId(UUID citizenId);
    List<CitizenFeedback> findTop20ByOrderByCreatedAtDesc();
}
