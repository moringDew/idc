package com.example.synergy.service.impl;

import com.example.synergy.entity.Notification;
import com.example.synergy.repository.NotificationRepository;
import com.example.synergy.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
    }

    @Override
    public long countUnread(String userId) {
        return notificationRepository.countByUserIdAndRead(userId, false);
    }

    @Override
    public Notification getById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
        });
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}