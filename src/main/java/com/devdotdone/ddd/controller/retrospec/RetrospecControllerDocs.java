package com.devdotdone.ddd.controller.retrospec;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.devdotdone.ddd.dto.retrospec.RetrospecRequest;
import com.devdotdone.ddd.dto.retrospec.RetrospecResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회고", description = "회고 관련 API")
public interface RetrospecControllerDocs {
    @Operation(summary = "회고 등록", description = "회고를 등록합니다")
    String create(@RequestBody RetrospecRequest request);

    @Operation(summary = "회고 상세페이지", description = "작성한 상세페이지를 조회합니다")
    RetrospecResponse getById(@PathVariable("retroId") int retroId);

    @Operation(summary = "회고 전체 조회", description = "프로젝트별 회고를 조회합니다")
    List<RetrospecResponse> getByProject(@PathVariable("projectId") int projectId);

    @Operation(summary = "회고 수정", description = "회고를 수정합니다")
    String update(@PathVariable("retroId") int retroId, @RequestBody RetrospecRequest request);

    @Operation(summary = "회고 삭제", description = "회고를 삭제합니다")
    String delete(@PathVariable("retroId") int retroId,
                         @RequestParam("projectId") int projectId,
                         @RequestParam("userId") int userId);
}
