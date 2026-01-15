package com.devdotdone.ddd.controller.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectRequest;
import com.devdotdone.ddd.dto.project.ProjectResponse;
import com.devdotdone.ddd.dto.schedule.Schedule;
import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;
import com.devdotdone.ddd.interceptor.Login;
import com.devdotdone.ddd.service.ProjectService;
import com.devdotdone.ddd.service.ScheduleService;
import com.devdotdone.ddd.service.UserProjectRoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectController {
  @Autowired
  private ProjectService projectService;

  @Autowired
  private ScheduleService scheduleService;

  @Autowired
  private UserProjectRoleService userProjectRoleService;

  @Login
  @PostMapping("/create")
  public Map<String, Object> create(@RequestBody ProjectRequest request) {
    log.info(request.toString());
    Map<String, Object> map = new HashMap<>();
    try {
      Project response = projectService.create(request);
      map.put("result", "success");
      map.put("data", response);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/list")
  public List<Project> list() {
    List<Project> list = projectService.getAllProjects();
    log.info("프로젝트 목록 조회", list);
    return list;
  }
 @Login
  @GetMapping("/detail")
  public Map<String, Object> detail(@RequestParam("projectId") int projectId) {
    Map<String, Object> map = new HashMap<>();
    Project project = projectService.getProjectById(projectId);
    if (project != null) {
      map.put("result", "success");
      map.put("data", project);
    } else {
      map.put("result", "fail");
      map.put("message", "해당 프로젝트가 존재하지 않습니다.");
    }
    return map;
  }

  @GetMapping("/schedules")
  public Map<String, Object> projectSchedules(@RequestParam("projectId") int projectId) {
    Map<String, Object> map = new HashMap<>();
    try {
      List<Schedule> schedules = scheduleService.getListByProject(projectId);
      map.put("result", "success");
      map.put("data", schedules);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/userprojectroles")
  public Map<String, Object> projectUserProjectRoles(@RequestParam("projectId") int projectId) {
    Map<String, Object> map = new HashMap<>();
    try {
      List<UserProjectRole> upr = userProjectRoleService.getProjectMember(projectId);
      map.put("result", "success");
      map.put("data", upr);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }
 @Login
  @PutMapping("/update")
  public ProjectResponse update(@RequestBody ProjectRequest request) {
     try {
    return projectService.update(request);
  } catch (DataIntegrityViolationException e) {
    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 프로젝트 멤버입니다.", e);
  } catch (IllegalArgumentException | IllegalStateException e) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
  }
  }
 @Login
  @DeleteMapping("/delete")
  public Map<String, Object> delete(@RequestParam("projectId") int projectId) {
    Map<String, Object> map = new HashMap<>();

    try {
      int rows = projectService.remove(projectId);
      if (rows == 0) {
        map.put("result", "fail");
        map.put("message", "삭제 실패");
      } else {
        map.put("result", "success");
        map.put("message", "프로젝트가 삭제되었습니다.");
      }
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }
 @Login
  @GetMapping("/list/user")
  public List<Project> getUserProjects(@RequestParam("userId") int userId) {
    return projectService.getProjectsByUserId(userId);
  }

}
