package com.devdotdone.ddd.controller.userprojectrole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;
import com.devdotdone.ddd.service.UserProjectRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "UserProjectRole", description = "UserProjectRole API")
public interface UserProjectRoleControllerDocs {

  @Operation(summary = "프로젝트 멤버 목록 조회", description = "프로젝트 멤버 목록 조회합니다.")
  List<UserProjectRole> userProjectRoleList(@RequestParam("projectId") int projectId);

 
  @Operation(summary = "프로젝트 멤버 신규 영입", description = "새로운 멤버 영입(총인원 6명인 조건 그대로)합니다.")
  Map<String, String> addMember(@RequestBody UserProjectRole userProjectRole);

  
  @Operation(summary = "팀장 권한 위임", description = "팀장 권한 위임합니다.")
  public Map<String, Object> changeAdmin(@RequestBody UserProjectRole userProjectRole);

  
  @Operation(summary = "프로젝트 멤버 수 조회", description = "프로젝트 총 인원수를 셉니다.")
  public Map<String, Object> countProjectMembers(@RequestParam("projectId") int projectId);

  // 프로젝터에서 멤버 빼기
  @Operation(summary = "프로젝터에서 멤버 빼기", description = "같은 프로젝트 팀 멤버를 방출시킵니다.")
  public String deleteMembers(@RequestParam("projectId") int projectId, @RequestParam("userId") int userId);

}
