package com.civicpulse.budget.repository;

import com.civicpulse.budget.domain.DepartmentBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentBudgetRepository extends JpaRepository<DepartmentBudget, UUID> {
    Optional<DepartmentBudget> findByDepartmentNameIgnoreCase(String name);
}
