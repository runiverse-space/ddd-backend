package com.devdotdone.ddd.controller.tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.devdotdone.ddd.dto.tag.ProjectTagRequest;
import com.devdotdone.ddd.dto.tag.ProjectTagResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "프로젝트 태그", description = "프로젝트 태그 관련 API")
public interface ProjectTagControllerDocs {
    @Operation(summary = "프로젝트 태그 등록", description = "프로젝트 생성에서 태그를 등록합니다")
    ProjectTagResponse create(@RequestBody ProjectTagRequest request);

    @Operation(summary = "프로젝트 태그 삭제", description = "프로젝트 태그를 삭제합니다")
    ProjectTagResponse delete(@RequestBody ProjectTagRequest request);

    @Operation(summary = "프로젝트 태그 조회", description = "프로젝트 태그 목록을 조회합니다")
    ProjectTagResponse getProjectTags(@PathVariable("projectId") int projectId);
}
