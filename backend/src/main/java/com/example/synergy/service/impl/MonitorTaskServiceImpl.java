package com.example.synergy.service.impl;

import com.example.synergy.entity.MonitorTask;
import com.example.synergy.repository.MonitorTaskRepository;
import com.example.synergy.service.MonitorTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonitorTaskServiceImpl implements MonitorTaskService {

    @Autowired
    private MonitorTaskRepository monitorTaskRepository;

    @Override
    public List<MonitorTask> findAll() {
        return monitorTaskRepository.findAll();
    }

    @Override
    public Optional<MonitorTask> findById(Long id) {
        return monitorTaskRepository.findById(id);
    }

    @Override
    public MonitorTask save(MonitorTask task) {
        return monitorTaskRepository.save(task);
    }

    @Override
    public void deleteById(Long id) {
        monitorTaskRepository.deleteById(id);
    }

    @Override
    public List<MonitorTask> findByStatus(String status) {
        return monitorTaskRepository.findByStatus(status);
    }

    @Override
    public List<MonitorTask> findByPriority(String priority) {
        return monitorTaskRepository.findByPriority(priority);
    }

    @Override
    public List<MonitorTask> findByDepartment(String department) {
        return monitorTaskRepository.findByDepartment(department);
    }

    @Override
    public MonitorTask updateProgress(Long id, Integer progress) {
        return monitorTaskRepository.findById(id)
                .map(task -> {
                    task.setProgress(progress);
                    if (progress >= 100) {
                        task.setStatus("completed");
                    }
                    return monitorTaskRepository.save(task);
                })
                .orElse(null);
    }
}