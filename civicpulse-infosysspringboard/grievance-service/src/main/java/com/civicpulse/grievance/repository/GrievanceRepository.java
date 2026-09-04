package com.civicpulse.grievance.repository;

import com.civicpulse.grievance.domain.Grievance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, UUID> {
    List<Grievance> findByCitizenId(UUID citizenId);
    List<Grievance> findByStatus(Grievance.Status status);
    List<Grievance> findByAssignedDepartment(Grievance.Department department);
    List<Grievance> findByStatusNotAndDueDateBefore(Grievance.Status status, LocalDateTime dateTime);
}
