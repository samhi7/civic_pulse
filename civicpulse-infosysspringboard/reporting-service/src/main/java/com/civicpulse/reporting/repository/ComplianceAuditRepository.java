package com.civicpulse.reporting.repository;

import com.civicpulse.reporting.domain.ComplianceAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceAuditRepository extends JpaRepository<ComplianceAudit, UUID> {
    List<ComplianceAudit> findTop20ByOrderByTimestampDesc();
}
