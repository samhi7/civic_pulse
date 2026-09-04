package com.civicpulse.reporting.repository;

import com.civicpulse.reporting.domain.DepartmentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentPerformanceRepository extends JpaRepository<DepartmentPerformance, UUID> {
    Optional<DepartmentPerformance> findByDepartmentNameIgnoreCase(String departmentName);
}
