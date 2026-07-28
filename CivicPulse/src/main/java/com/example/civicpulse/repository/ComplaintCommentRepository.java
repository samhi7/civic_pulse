package com.example.civicpulse.repository;

import com.example.civicpulse.model.ComplaintComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintCommentRepository extends JpaRepository<ComplaintComment, Long> {
}
