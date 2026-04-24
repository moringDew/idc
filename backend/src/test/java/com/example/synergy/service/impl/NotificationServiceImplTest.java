package com.example.synergy.service.impl;

import com.example.synergy.entity.Notification;
import com.example.synergy.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("测试创建通知")
    void testCreate() {
        Notification notification = new Notification();
        notification.setTitle("测试通知");
        notification.setContent("测试内容");
        notification.setUserId("1");
        notification.setType("info");

        Notification savedNotification = new Notification();
        savedNotification.setId(1L);
        savedNotification.setTitle("测试通知");
        savedNotification.setRead(false);

        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        Notification result = notificationService.create(notification);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertFalse(result.getRead());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    @DisplayName("测试获取用户通知列表")
    void testGetNotifications() {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setUserId("1");

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setUserId("1");

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc("1")).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> result = notificationService.getNotifications("1");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findByUserIdOrderByCreatedAtDesc("1");
    }

    @Test
    @DisplayName("测试获取未读通知列表")
    void testGetUnreadNotifications() {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setUserId("1");
        notification1.setRead(false);

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setUserId("1");
        notification2.setRead(false);

        when(notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc("1", false)).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> result = notificationService.getUnreadNotifications("1");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findByUserIdAndReadOrderByCreatedAtDesc("1", false);
    }

    @Test
    @DisplayName("测试统计未读数量")
    void testCountUnread() {
        when(notificationRepository.countByUserIdAndRead("1", false)).thenReturn(5L);

        long result = notificationService.countUnread("1");

        assertEquals(5, result);
        verify(notificationRepository, times(1)).countByUserIdAndRead("1", false);
    }

    @Test
    @DisplayName("测试标记单条通知为已读")
    void testMarkAsRead() {
        Long id = 1L;
        Notification notification = new Notification();
        notification.setId(id);
        notification.setRead(false);

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.markAsRead(id);

        assertTrue(notification.getRead());
        verify(notificationRepository, times(1)).findById(id);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    @DisplayName("测试标记所有通知为已读")
    void testMarkAllAsRead() {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setUserId("1");
        notification1.setRead(false);

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setUserId("1");
        notification2.setRead(false);

        when(notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc("1", false)).thenReturn(Arrays.asList(notification1, notification2));
        when(notificationRepository.saveAll(anyList())).thenReturn(Arrays.asList(notification1, notification2));

        notificationService.markAllAsRead("1");

        assertTrue(notification1.getRead());
        assertTrue(notification2.getRead());
        verify(notificationRepository, times(1)).findByUserIdAndReadOrderByCreatedAtDesc("1", false);
        verify(notificationRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("测试删除通知")
    void testDelete() {
        Long id = 1L;
        Notification notification = new Notification();
        notification.setId(id);

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        doNothing().when(notificationRepository).deleteById(id);

        notificationService.delete(id);

        verify(notificationRepository, times(1)).findById(id);
        verify(notificationRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("测试根据ID获取通知")
    void testGetById_Success() {
        Long id = 1L;
        Notification notification = new Notification();
        notification.setId(id);

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        Notification result = notificationService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(notificationRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("测试根据ID获取通知 - 不存在")
    void testGetById_NotFound() {
        Long id = 999L;

        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        assertNull(notificationService.getById(id));
        verify(notificationRepository, times(1)).findById(id);
    }
}