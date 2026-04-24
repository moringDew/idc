package com.example.synergy.service.impl;

import com.example.synergy.entity.Knowledge;
import com.example.synergy.repository.KnowledgeRepository;
import com.example.synergy.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Override
    public List<Knowledge> findAll() {
        return knowledgeRepository.findAll();
    }

    @Override
    public Optional<Knowledge> findById(Long id) {
        return knowledgeRepository.findById(id);
    }

    @Override
    public Knowledge save(Knowledge knowledge) {
        return knowledgeRepository.save(knowledge);
    }

    @Override
    public void deleteById(Long id) {
        knowledgeRepository.deleteById(id);
    }

    @Override
    public List<Knowledge> findByFolder(String folder) {
        return knowledgeRepository.findByFolder(folder);
    }

    @Override
    public List<Knowledge> findByName(String name) {
        return knowledgeRepository.findByNameContaining(name);
    }
}