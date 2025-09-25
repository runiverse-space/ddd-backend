package com.devdotdone.ddd.controller.project;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.devdotdone.ddd.dto.project.ProjectMilestone;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="마일스톤", description = "프로젝트 마일스톤 관련 API")
public interface ProjectMilestoneControllerDocs {

  @Operation(summary = "마일스톤 생성", description = "새로운 마일스톤을 생성합니다")
  Map<String, Object> create(@RequestBody ProjectMilestone projectMilestone);

  @Operation(summary = "마일스톤 목록", description = "프로젝트에 등록된 마일스톤을 모두 조회합니다")
  Map<String, Object> list(@RequestParam("projectId") int projectId);

  @Operation(summary = "마일스톤 조회", description = "마일스톤의 아이디로 마일스톤을 조회합니다")
  Map<String, Object> detail(@RequestParam("milestoneId") int milestoneId);

  @Operation(summary = "마일스톤 수정", description = "마일스톤을 수정합니다")
  Map<String, Object> update(@RequestBody ProjectMilestone milestone);

  @Operation(summary = "마일스톤 삭제", description = "마일스톤을 삭제합니다")
  Map<String, Object> delete(@RequestParam("milestoneId") int milestoneId);
}
