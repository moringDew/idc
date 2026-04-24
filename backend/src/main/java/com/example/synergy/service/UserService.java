package com.example.synergy.service;

import com.example.synergy.entity.SysUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<SysUser> login(String username, String password);
    List<SysUser> findAll();
    Optional<SysUser> findById(Long id);
    Optional<SysUser> findByUsername(String username);
    SysUser save(SysUser user);
    void deleteById(Long id);
    void toggleStatus(Long id);
}