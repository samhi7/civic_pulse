package com.civicpulse.welfare.repository;

import com.civicpulse.welfare.domain.WelfareScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WelfareSchemeRepository extends JpaRepository<WelfareScheme, UUID> {
    Optional<WelfareScheme> findByName(String name);
}
