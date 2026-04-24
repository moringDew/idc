package com.example.synergy.repository;

import com.example.synergy.entity.MonitorTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitorTaskRepository extends JpaRepository<MonitorTask, Long> {
    List<MonitorTask> findByStatus(String status);
    List<MonitorTask> findByPriority(String priority);
    List<MonitorTask> findByDepartment(String department);
}