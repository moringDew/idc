package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.MonitorTask;
import com.example.synergy.service.MonitorTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorTaskController {

    @Autowired
    private MonitorTaskService monitorTaskService;

    @GetMapping
    public Result<List<MonitorTask>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String department) {
        List<MonitorTask> tasks;
        if (status != null && !status.isEmpty()) {
            tasks = monitorTaskService.findByStatus(status);
        } else if (priority != null && !priority.isEmpty()) {
            tasks = monitorTaskService.findByPriority(priority);
        } else if (department != null && !department.isEmpty()) {
            tasks = monitorTaskService.findByDepartment(department);
        } else {
            tasks = monitorTaskService.findAll();
        }
        return Result.success(tasks);
    }

    @GetMapping("/{id}")
    public Result<MonitorTask> getTaskById(@PathVariable Long id) {
        return monitorTaskService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "任务不存在"));
    }

    @PostMapping
    public Result<MonitorTask> createTask(@RequestBody MonitorTask task) {
        MonitorTask saved = monitorTaskService.save(task);
        return Result.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    public Result<MonitorTask> updateTask(@PathVariable Long id, @RequestBody MonitorTask task) {
        return monitorTaskService.findById(id)
                .map(existing -> {
                    if (task.getTitle() != null) existing.setTitle(task.getTitle());
                    if (task.getDepartment() != null) existing.setDepartment(task.getDepartment());
                    if (task.getPerson() != null) existing.setPerson(task.getPerson());
                    if (task.getDeadline() != null) existing.setDeadline(task.getDeadline());
                    if (task.getPriority() != null) existing.setPriority(task.getPriority());
                    if (task.getProgress() != null) existing.setProgress(task.getProgress());
                    MonitorTask saved = monitorTaskService.save(existing);
                    return Result.success("更新成功", saved);
                })
                .orElse(Result.error(404, "任务不存在"));
    }

    @PutMapping("/{id}/progress")
    public Result<MonitorTask> updateProgress(@PathVariable Long id, @RequestParam Integer progress) {
        MonitorTask updated = monitorTaskService.updateProgress(id, progress);
        if (updated == null) {
            return Result.error(404, "任务不存在");
        }
        return Result.success("进度已更新", updated);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteTask(@PathVariable Long id) {
        if (!monitorTaskService.findById(id).isPresent()) {
            return Result.error(404, "任务不存在");
        }
        monitorTaskService.deleteById(id);
        return Result.success("删除成功");
    }
}