package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.Notification;
import com.example.synergy.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/count")
    public Result<Map<String, Long>> getUnreadCount(@RequestParam String userId) {
        long count = notificationService.countUnread(userId);
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return Result.success(result);
    }

    @GetMapping
    public Result<List<Notification>> getNotifications(@RequestParam String userId) {
        List<Notification> notifications = notificationService.getNotifications(userId);
        return Result.success(notifications);
    }

    @GetMapping("/unread")
    public Result<List<Notification>> getUnreadNotifications(@RequestParam String userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return Result.success(notifications);
    }

    @PostMapping
    public Result<Notification> createNotification(@RequestBody Notification notification) {
        Notification created = notificationService.create(notification);
        return Result.success("创建成功", created);
    }

    @PutMapping("/{id}/read")
    public Result<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return Result.success("已标记为已读");
    }

    @PutMapping("/read-all")
    public Result<?> markAllAsRead(@RequestParam String userId) {
        notificationService.markAllAsRead(userId);
        return Result.success("已全部标记为已读");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteNotification(@PathVariable Long id) {
        notificationService.delete(id);
        return Result.success("删除成功");
    }
}