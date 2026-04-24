package com.example.synergy.repository;

import com.example.synergy.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByStatus(String status);
    List<Approval> findByApplicant(String applicant);
    List<Approval> findByType(String type);
}