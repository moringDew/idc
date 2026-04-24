package com.example.synergy.service;

import com.example.synergy.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    List<Document> findAll();
    Optional<Document> findById(Long id);
    Document save(Document document);
    void deleteById(Long id);
    List<Document> findByTitle(String title);
    List<Document> findByType(String type);
    List<Document> findByStatus(String status);
    List<Document> findByDepartment(String department);
    Document updateStatus(Long id, String status);
}