package com.civicpulse.service.repository;

import com.civicpulse.service.domain.Permit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermitRepository extends JpaRepository<Permit, UUID> {
    Optional<Permit> findByPermitNumber(String permitNumber);
    Optional<Permit> findByApplicationId(UUID applicationId);
}
