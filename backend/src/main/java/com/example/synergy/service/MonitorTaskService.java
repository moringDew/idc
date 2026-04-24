package com.example.synergy.service;

import com.example.synergy.entity.MonitorTask;

import java.util.List;
import java.util.Optional;

public interface MonitorTaskService {
    List<MonitorTask> findAll();
    Optional<MonitorTask> findById(Long id);
    MonitorTask save(MonitorTask task);
    void deleteById(Long id);
    List<MonitorTask> findByStatus(String status);
    List<MonitorTask> findByPriority(String priority);
    List<MonitorTask> findByDepartment(String department);
    MonitorTask updateProgress(Long id, Integer progress);
}