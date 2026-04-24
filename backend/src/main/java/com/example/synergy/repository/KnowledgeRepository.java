package com.example.synergy.repository;

import com.example.synergy.entity.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {
    List<Knowledge> findByFolder(String folder);
    List<Knowledge> findByNameContaining(String name);
}