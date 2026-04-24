package com.example.synergy.repository;

import com.example.synergy.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(String userId, Boolean read);
    
    long countByUserIdAndRead(String userId, Boolean read);
    
    void deleteByUserId(String userId);
}