package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.Document;
import com.example.synergy.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public Result<List<Document>> getAllDocuments(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        List<Document> documents;
        if (title != null && !title.isEmpty()) {
            documents = documentService.findByTitle(title);
        } else if (type != null && !type.isEmpty()) {
            documents = documentService.findByType(type);
        } else if (status != null && !status.isEmpty()) {
            documents = documentService.findByStatus(status);
        } else {
            documents = documentService.findAll();
        }
        return Result.success(documents);
    }

    @GetMapping("/{id}")
    public Result<Document> getDocumentById(@PathVariable Long id) {
        return documentService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "文档不存在"));
    }

    @PostMapping
    public Result<Document> createDocument(@RequestBody Document document) {
        document.setCode("2024-" + String.format("%03d", documentService.findAll().size() + 1));
        Document saved = documentService.save(document);
        return Result.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    public Result<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        return documentService.findById(id)
                .map(existing -> {
                    if (document.getTitle() != null) existing.setTitle(document.getTitle());
                    if (document.getType() != null) existing.setType(document.getType());
                    if (document.getDepartment() != null) existing.setDepartment(document.getDepartment());
                    if (document.getRecipient() != null) existing.setRecipient(document.getRecipient());
                    if (document.getContent() != null) existing.setContent(document.getContent());
                    Document saved = documentService.save(existing);
                    return Result.success("更新成功", saved);
                })
                .orElse(Result.error(404, "文档不存在"));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteDocument(@PathVariable Long id) {
        if (!documentService.findById(id).isPresent()) {
            return Result.error(404, "文档不存在");
        }
        documentService.deleteById(id);
        return Result.success("删除成功");
    }

    @PutMapping("/{id}/status")
    public Result<Document> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Document updated = documentService.updateStatus(id, status);
        if (updated == null) {
            return Result.error(404, "文档不存在");
        }
        return Result.success("状态已更新", updated);
    }
}