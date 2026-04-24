package com.example.synergy.service.impl;

import com.example.synergy.entity.Document;
import com.example.synergy.repository.DocumentRepository;
import com.example.synergy.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    public List<Document> findByTitle(String title) {
        return documentRepository.findByTitleContaining(title);
    }

    @Override
    public List<Document> findByType(String type) {
        return documentRepository.findByType(type);
    }

    @Override
    public List<Document> findByStatus(String status) {
        return documentRepository.findByStatus(status);
    }

    @Override
    public List<Document> findByDepartment(String department) {
        return documentRepository.findByDepartment(department);
    }

    @Override
    public Document updateStatus(Long id, String status) {
        return documentRepository.findById(id)
                .map(doc -> {
                    doc.setStatus(status);
                    return documentRepository.save(doc);
                })
                .orElse(null);
    }
}