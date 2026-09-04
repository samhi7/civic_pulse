package com.civicpulse.welfare.repository;

import com.civicpulse.welfare.domain.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, UUID> {
    List<Beneficiary> findBySchemeId(UUID schemeId);
    Optional<Beneficiary> findBySchemeIdAndCitizenId(UUID schemeId, UUID citizenId);
}
