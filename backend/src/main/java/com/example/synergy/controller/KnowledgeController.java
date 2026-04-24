package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.Knowledge;
import com.example.synergy.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @GetMapping
    public Result<List<Knowledge>> getAllKnowledge(
            @RequestParam(required = false) String folder,
            @RequestParam(required = false) String name) {
        List<Knowledge> knowledgeList;
        if (folder != null && !folder.isEmpty()) {
            knowledgeList = knowledgeService.findByFolder(folder);
        } else if (name != null && !name.isEmpty()) {
            knowledgeList = knowledgeService.findByName(name);
        } else {
            knowledgeList = knowledgeService.findAll();
        }
        return Result.success(knowledgeList);
    }

    @GetMapping("/{id}")
    public Result<Knowledge> getKnowledgeById(@PathVariable Long id) {
        return knowledgeService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "文档不存在"));
    }

    @PostMapping
    public Result<Knowledge> createKnowledge(@RequestBody Knowledge knowledge) {
        Knowledge saved = knowledgeService.save(knowledge);
        return Result.success("上传成功", saved);
    }

    @PutMapping("/{id}")
    public Result<Knowledge> updateKnowledge(@PathVariable Long id, @RequestBody Knowledge knowledge) {
        return knowledgeService.findById(id)
                .map(existing -> {
                    if (knowledge.getName() != null) existing.setName(knowledge.getName());
                    if (knowledge.getType() != null) existing.setType(knowledge.getType());
                    if (knowledge.getSize() != null) existing.setSize(knowledge.getSize());
                    if (knowledge.getFolder() != null) existing.setFolder(knowledge.getFolder());
                    if (knowledge.getDescription() != null) existing.setDescription(knowledge.getDescription());
                    Knowledge saved = knowledgeService.save(existing);
                    return Result.success("更新成功", saved);
                })
                .orElse(Result.error(404, "文档不存在"));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteKnowledge(@PathVariable Long id) {
        if (!knowledgeService.findById(id).isPresent()) {
            return Result.error(404, "文档不存在");
        }
        knowledgeService.deleteById(id);
        return Result.success("删除成功");
    }
}