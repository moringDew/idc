package com.example.synergy.service;

import com.example.synergy.entity.Notification;

import java.util.List;

public interface NotificationService {
    
    List<Notification> getNotifications(String userId);
    
    List<Notification> getUnreadNotifications(String userId);
    
    long countUnread(String userId);
    
    Notification getById(Long id);
    
    Notification create(Notification notification);
    
    void markAsRead(Long id);
    
    void markAllAsRead(String userId);
    
    void delete(Long id);
}