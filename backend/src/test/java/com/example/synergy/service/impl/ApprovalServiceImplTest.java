package com.example.synergy.service.impl;

import com.example.synergy.entity.Approval;
import com.example.synergy.repository.ApprovalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApprovalServiceImplTest {

    @Mock
    private ApprovalRepository approvalRepository;

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("测试创建审批")
    void testCreate() {
        Approval approval = new Approval();
        approval.setTitle("测试审批");
        approval.setContent("测试内容");
        approval.setApplicantId("1");
        approval.setApplicantName("张三");
        approval.setType("采购");

        Approval savedApproval = new Approval();
        savedApproval.setId(1L);
        savedApproval.setTitle("测试审批");
        savedApproval.setStatus("pending");
        savedApproval.setCreatedAt(LocalDateTime.now());

        when(approvalRepository.save(any(Approval.class))).thenReturn(savedApproval);

        Approval result = approvalService.create(approval);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("pending", result.getStatus());
        verify(approvalRepository, times(1)).save(any(Approval.class));
    }

    @Test
    @DisplayName("测试获取所有审批")
    void testFindAll() {
        Approval approval1 = new Approval();
        approval1.setId(1L);
        approval1.setTitle("审批1");

        Approval approval2 = new Approval();
        approval2.setId(2L);
        approval2.setTitle("审批2");

        when(approvalRepository.findAll()).thenReturn(Arrays.asList(approval1, approval2));

        List<Approval> result = approvalService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(approvalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("测试根据ID获取审批")
    void testFindById_Success() {
        Long id = 1L;
        Approval approval = new Approval();
        approval.setId(id);
        approval.setTitle("测试审批");

        when(approvalRepository.findById(id)).thenReturn(Optional.of(approval));

        Approval result = approvalService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(approvalRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("测试根据ID获取审批 - 不存在")
    void testFindById_NotFound() {
        Long id = 999L;

        when(approvalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> approvalService.findById(id));
        verify(approvalRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("测试审批通过")
    void testApprove() {
        Long id = 1L;
        Approval approval = new Approval();
        approval.setId(id);
        approval.setStatus("pending");

        when(approvalRepository.findById(id)).thenReturn(Optional.of(approval));
        when(approvalRepository.save(any(Approval.class))).thenReturn(approval);

        approvalService.approve(id, "admin");

        assertEquals("approved", approval.getStatus());
        assertNotNull(approval.getApproverId());
        assertNotNull(approval.getApprovedAt());
        verify(approvalRepository, times(1)).findById(id);
        verify(approvalRepository, times(1)).save(any(Approval.class));
    }

    @Test
    @DisplayName("测试审批驳回")
    void testReject() {
        Long id = 1L;
        Approval approval = new Approval();
        approval.setId(id);
        approval.setStatus("pending");

        when(approvalRepository.findById(id)).thenReturn(Optional.of(approval));
        when(approvalRepository.save(any(Approval.class))).thenReturn(approval);

        approvalService.reject(id, "admin", "理由");

        assertEquals("rejected", approval.getStatus());
        assertEquals("理由", approval.getRejectReason());
        assertNotNull(approval.getApproverId());
        assertNotNull(approval.getApprovedAt());
        verify(approvalRepository, times(1)).findById(id);
        verify(approvalRepository, times(1)).save(any(Approval.class));
    }

    @Test
    @DisplayName("测试获取待审批列表")
    void testGetPendingApprovals() {
        Approval approval1 = new Approval();
        approval1.setId(1L);
        approval1.setStatus("pending");

        Approval approval2 = new Approval();
        approval2.setId(2L);
        approval2.setStatus("pending");

        when(approvalRepository.findByStatus("pending")).thenReturn(Arrays.asList(approval1, approval2));

        List<Approval> result = approvalService.getPendingApprovals();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(approvalRepository, times(1)).findByStatus("pending");
    }

    @Test
    @DisplayName("测试删除审批")
    void testDelete() {
        Long id = 1L;
        Approval approval = new Approval();
        approval.setId(id);

        when(approvalRepository.findById(id)).thenReturn(Optional.of(approval));
        doNothing().when(approvalRepository).deleteById(id);

        approvalService.delete(id);

        verify(approvalRepository, times(1)).findById(id);
        verify(approvalRepository, times(1)).deleteById(id);
    }
}