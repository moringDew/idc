package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.Approval;
import com.example.synergy.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @GetMapping
    public Result<List<Approval>> getAllApprovals(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String applicant) {
        List<Approval> approvals;
        if (status != null && !status.isEmpty()) {
            approvals = approvalService.findByStatus(status);
        } else if (applicant != null && !applicant.isEmpty()) {
            approvals = approvalService.findByApplicant(applicant);
        } else {
            approvals = approvalService.findAll();
        }
        return Result.success(approvals);
    }

    @GetMapping("/{id}")
    public Result<Approval> getApprovalById(@PathVariable Long id) {
        return approvalService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "审批记录不存在"));
    }

    @PostMapping
    public Result<Approval> createApproval(@RequestBody Approval approval) {
        Approval saved = approvalService.save(approval);
        return Result.success("创建成功", saved);
    }

    @PutMapping("/{id}/approve")
    public Result<Approval> approve(@PathVariable Long id) {
        Approval updated = approvalService.approve(id);
        if (updated == null) {
            return Result.error(404, "审批记录不存在");
        }
        return Result.success("已同意", updated);
    }

    @PutMapping("/{id}/reject")
    public Result<Approval> reject(@PathVariable Long id) {
        Approval updated = approvalService.reject(id);
        if (updated == null) {
            return Result.error(404, "审批记录不存在");
        }
        return Result.success("已驳回", updated);
    }
}