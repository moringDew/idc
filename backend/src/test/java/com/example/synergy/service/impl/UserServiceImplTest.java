package com.example.synergy.service.impl;

import com.example.synergy.entity.SysUser;
import com.example.synergy.repository.SysUserRepository;
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

class UserServiceImplTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("测试登录 - 用户名密码正确")
    void testLogin_Success() {
        String username = "admin";
        String password = "123456";
        
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        user.setName("管理员");
        user.setRole("admin");
        user.setDepartment("办公室");
        user.setStatus("active");

        when(sysUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        SysUser result = userService.login(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("管理员", result.getName());
        verify(sysUserRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("测试登录 - 用户不存在")
    void testLogin_UserNotFound() {
        String username = "nonexistent";
        String password = "123456";

        when(sysUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login(username, password));
        verify(sysUserRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("测试登录 - 密码错误")
    void testLogin_WrongPassword() {
        String username = "admin";
        String password = "wrongpassword";

        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");

        when(sysUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.login(username, password));
        verify(sysUserRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("测试获取所有用户")
    void testFindAll() {
        SysUser user1 = new SysUser();
        user1.setId(1L);
        user1.setUsername("admin");

        SysUser user2 = new SysUser();
        user2.setId(2L);
        user2.setUsername("user");

        when(sysUserRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<SysUser> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sysUserRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("测试根据ID获取用户 - 用户存在")
    void testFindById_Success() {
        Long userId = 1L;
        SysUser user = new SysUser();
        user.setId(userId);
        user.setUsername("admin");

        when(sysUserRepository.findById(userId)).thenReturn(Optional.of(user));

        SysUser result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(sysUserRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("测试根据ID获取用户 - 用户不存在")
    void testFindById_NotFound() {
        Long userId = 999L;

        when(sysUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(userId));
        verify(sysUserRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("测试创建用户")
    void testCreate() {
        SysUser user = new SysUser();
        user.setUsername("newuser");
        user.setPassword("123456");
        user.setName("新用户");

        SysUser savedUser = new SysUser();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setPassword("e10adc3949ba59abbe56e057f20f883e");

        when(sysUserRepository.save(any(SysUser.class))).thenReturn(savedUser);

        SysUser result = userService.create(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.getPassword().length() > 20);
        verify(sysUserRepository, times(1)).save(any(SysUser.class));
    }

    @Test
    @DisplayName("测试创建用户 - 用户已存在")
    void testCreate_UserExists() {
        SysUser user = new SysUser();
        user.setUsername("admin");
        user.setPassword("123456");

        when(sysUserRepository.existsByUsername("admin")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
        verify(sysUserRepository, times(1)).existsByUsername("admin");
        verify(sysUserRepository, never()).save(any(SysUser.class));
    }

    @Test
    @DisplayName("测试更新用户")
    void testUpdate() {
        Long userId = 1L;
        SysUser existingUser = new SysUser();
        existingUser.setId(userId);
        existingUser.setUsername("admin");
        existingUser.setPassword("oldpassword");
        existingUser.setName("管理员");

        SysUser updateUser = new SysUser();
        updateUser.setName("新管理员");
        updateUser.setDepartment("新部门");

        when(sysUserRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(sysUserRepository.save(any(SysUser.class))).thenReturn(existingUser);

        SysUser result = userService.update(userId, updateUser);

        assertNotNull(result);
        assertEquals("新管理员", result.getName());
        assertEquals("新部门", result.getDepartment());
        verify(sysUserRepository, times(1)).findById(userId);
        verify(sysUserRepository, times(1)).save(any(SysUser.class));
    }

    @Test
    @DisplayName("测试删除用户")
    void testDelete() {
        Long userId = 1L;
        SysUser user = new SysUser();
        user.setId(userId);

        when(sysUserRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(sysUserRepository).deleteById(userId);

        userService.delete(userId);

        verify(sysUserRepository, times(1)).findById(userId);
        verify(sysUserRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("测试删除用户 - 用户不存在")
    void testDelete_NotFound() {
        Long userId = 999L;

        when(sysUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.delete(userId));
        verify(sysUserRepository, times(1)).findById(userId);
        verify(sysUserRepository, never()).deleteById(userId);
    }
}