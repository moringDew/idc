package com.example.synergy.service;

import com.example.synergy.entity.Knowledge;

import java.util.List;
import java.util.Optional;

public interface KnowledgeService {
    List<Knowledge> findAll();
    Optional<Knowledge> findById(Long id);
    Knowledge save(Knowledge knowledge);
    void deleteById(Long id);
    List<Knowledge> findByFolder(String folder);
    List<Knowledge> findByName(String name);
}