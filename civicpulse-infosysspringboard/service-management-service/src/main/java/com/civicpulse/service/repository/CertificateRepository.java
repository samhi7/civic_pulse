package com.civicpulse.service.repository;

import com.civicpulse.service.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
    Optional<Certificate> findByApplicationId(UUID applicationId);
}
