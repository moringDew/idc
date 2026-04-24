package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.SysUser;
import com.example.synergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<List<SysUser>> getAllUsers() {
        List<SysUser> users = userService.findAll();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> {
                    user.setPassword(null);
                    return Result.success(user);
                })
                .orElseGet(() -> Result.error(404, "用户不存在"));
    }

    @PostMapping
    public Result<SysUser> createUser(@RequestBody SysUser user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return Result.error(400, "用户名已存在");
        }
        SysUser saved = userService.save(user);
        saved.setPassword(null);
        return Result.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    public Result<SysUser> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        Optional<SysUser> existingOpt = userService.findById(id);
        if (!existingOpt.isPresent()) {
            return Result.error(404, "用户不存在");
        }
        SysUser existing = existingOpt.get();
        if (user.getUsername() != null && !user.getUsername().equals(existing.getUsername())) {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                return Result.error(400, "用户名已存在");
            }
            existing.setUsername(user.getUsername());
        }
        if (user.getName() != null) existing.setName(user.getName());
        if (user.getDepartment() != null) existing.setDepartment(user.getDepartment());
        if (user.getRole() != null) existing.setRole(user.getRole());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(user.getPassword());
        }
        SysUser saved = userService.save(existing);
        saved.setPassword(null);
        return Result.success("更新成功", saved);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            return Result.error(404, "用户不存在");
        }
        userService.deleteById(id);
        return Result.success("删除成功");
    }

    @PutMapping("/{id}/status")
    public Result<?> toggleStatus(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            return Result.error(404, "用户不存在");
        }
        userService.toggleStatus(id);
        return Result.success("状态已切换");
    }
}