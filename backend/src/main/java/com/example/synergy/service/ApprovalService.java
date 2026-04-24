package com.example.synergy.service;

import com.example.synergy.entity.Approval;

import java.util.List;
import java.util.Optional;

public interface ApprovalService {
    List<Approval> findAll();
    Optional<Approval> findById(Long id);
    Approval save(Approval approval);
    void deleteById(Long id);
    List<Approval> findByStatus(String status);
    List<Approval> findByApplicant(String applicant);
    Approval approve(Long id);
    Approval reject(Long id);
}