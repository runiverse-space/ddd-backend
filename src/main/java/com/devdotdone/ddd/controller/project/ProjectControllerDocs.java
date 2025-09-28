package com.devdotdone.ddd.controller.project;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectMilestone;
import com.devdotdone.ddd.dto.project.ProjectRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="프로젝트", description = "프로젝트 관련 API")
public interface ProjectControllerDocs {

  @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다")
  public Map<String, Object> create(@RequestBody ProjectRequest request);
  
  @Operation(summary = "프로젝트 조회", description = "프로젝트의 아이디로 프로젝트를 조회합니다")
  Map<String, Object> detail(@RequestParam("projectId") int projectId);

  @Operation(summary = "프로젝트 참여자 목록", description = "프로젝트에 참여한 사용자들을 조회합니다")
  Map<String, Object> projectSchedules(@RequestParam("projectId") int projectId);


  @Operation(summary = "프로젝트 수정", description = "프로젝트를 수정합니다")
  Map<String, Object> update(@RequestBody ProjectRequest request);

  @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다")
  Map<String, Object> delete(@RequestParam("projectId") int projectId);
}
