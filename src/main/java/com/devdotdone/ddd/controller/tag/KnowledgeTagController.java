package com.devdotdone.ddd.controller.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.KnowledgeTagRequest;
import com.devdotdone.ddd.dto.tag.KnowledgeTagResponse;
import com.devdotdone.ddd.service.KnowledgeTagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/knowledgeTag")
public class KnowledgeTagController {
    @Autowired
    private KnowledgeTagService knowledgeTagService;

    @PostMapping("/update")
    public KnowledgeTagResponse update(@RequestBody KnowledgeTagRequest request) {
        return knowledgeTagService.update(request);
    }

    @GetMapping("/{knowledgeId}")
    public KnowledgeTagResponse getKnowledgeTags(@PathVariable ("knowledgeId") int knowledgeId) {
        return knowledgeTagService.getKnowledgeTags(knowledgeId);
    }
}
