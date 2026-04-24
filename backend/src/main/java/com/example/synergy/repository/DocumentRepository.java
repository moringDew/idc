package com.example.synergy.repository;

import com.example.synergy.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTitleContaining(String title);
    List<Document> findByType(String type);
    List<Document> findByStatus(String status);
    List<Document> findByDepartment(String department);
}