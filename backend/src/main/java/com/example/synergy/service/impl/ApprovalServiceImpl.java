package com.example.synergy.service.impl;

import com.example.synergy.entity.Approval;
import com.example.synergy.repository.ApprovalRepository;
import com.example.synergy.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Override
    public List<Approval> findAll() {
        return approvalRepository.findAll();
    }

    @Override
    public Optional<Approval> findById(Long id) {
        return approvalRepository.findById(id);
    }

    @Override
    public Approval save(Approval approval) {
        return approvalRepository.save(approval);
    }

    @Override
    public void deleteById(Long id) {
        approvalRepository.deleteById(id);
    }

    @Override
    public List<Approval> findByStatus(String status) {
        return approvalRepository.findByStatus(status);
    }

    @Override
    public List<Approval> findByApplicant(String applicant) {
        return approvalRepository.findByApplicant(applicant);
    }

    @Override
    public Approval approve(Long id) {
        return approvalRepository.findById(id)
                .map(app -> {
                    app.setStatus("approved");
                    return approvalRepository.save(app);
                })
                .orElse(null);
    }

    @Override
    public Approval reject(Long id) {
        return approvalRepository.findById(id)
                .map(app -> {
                    app.setStatus("rejected");
                    return approvalRepository.save(app);
                })
                .orElse(null);
    }
}