package com.example.synergy.service.impl;

import com.example.synergy.entity.SysUser;
import com.example.synergy.repository.SysUserRepository;
import com.example.synergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserRepository userRepository;

    @Override
    public Optional<SysUser> login(String username, String password) {
        String hashedPassword = md5(password);
        return userRepository.findByUsernameAndPassword(username, hashedPassword);
    }

    @Override
    public List<SysUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<SysUser> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<SysUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public SysUser save(SysUser user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(md5(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void toggleStatus(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatus("enabled".equals(user.getStatus()) ? "disabled" : "enabled");
            userRepository.save(user);
        });
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}