package com.civicpulse.citizen.repository;

import com.civicpulse.citizen.domain.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, UUID> {
    Optional<Citizen> findByAadharNumber(String aadharNumber);
    List<Citizen> findByNameContainingIgnoreCase(String name);
    List<Citizen> findByWard(String ward);
}
