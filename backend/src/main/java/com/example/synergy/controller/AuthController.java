package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.SysUser;
import com.example.synergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        return userService.login(username, password)
                .map(user -> {
                    user.setPassword(null);
                    return Result.success("登录成功", user);
                })
                .orElseGet(() -> Result.error(401, "用户名或密码错误"));
    }
}