package com.devdotdone.ddd.controller.tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.devdotdone.ddd.dto.tag.KnowledgeTagRequest;
import com.devdotdone.ddd.dto.tag.KnowledgeTagResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지식 태그", description = "지식 태그 관련 API")
public interface KnowledgeTagControllerDocs {
    @Operation(summary = "지식 태그 등록", description = "지식창고에 태그를 등록합니다")
    KnowledgeTagResponse create(@RequestBody KnowledgeTagRequest request);

    @Operation(summary = "지식 태그 삭제", description = "지식창고에서 태그를 삭제합니다")
    KnowledgeTagResponse delete(@RequestBody KnowledgeTagRequest request);

    @Operation(summary = "지식 태그 조회", description = "지식창고 태그 목록을 조회합니다")
    KnowledgeTagResponse getKnowledgeTags(@PathVariable("knowledgeId") int knowledgeId);
}
