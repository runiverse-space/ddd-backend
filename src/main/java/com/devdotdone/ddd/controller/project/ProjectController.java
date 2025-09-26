package com.devdotdone.ddd.controller.project;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectRequest;
import com.devdotdone.ddd.dto.schedule.Schedule;
import com.devdotdone.ddd.dto.schedule.ScheduleRequest;
import com.devdotdone.ddd.service.ProjectMilestoneService;
import com.devdotdone.ddd.service.ProjectService;
import com.devdotdone.ddd.service.UserProjectRoleService;
import com.devdotdone.ddd.service.UsersService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController implements ProjectControllerDocs {
  private final ProjectService projectService;
  private final UserProjectRoleService userProjectRoleService;
  private final UsersService usersService;
  private final ProjectMilestoneService projectMilestoneService;

  @PostMapping("/create")
  public Map<String, Object> create(@RequestBody ProjectRequest request) {
    Map<String, Object> map = new HashMap<>();
    try {
      projectService.create(request);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

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

  @PutMapping("/update")
  public Map<String, Object> update(@RequestBody ProjectRequest request) {
    Map<String, Object> map = new HashMap<>();
    try {
      Project project = projectService.update(request);
      if (project == null) {
        map.put("result", "fail");
        map.put("message", "해당 프로젝트가 존재하지 않습니다.");
      } else {
        map.put("result", "success");
        map.put("data", project);
      }

    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @DeleteMapping("/delete")
  public Map<String, Object> delete(@RequestParam("projectId") int projectId) {
    Map<String, Object> map = new HashMap<>();

    try {
      int rows = projectService.delete(projectId);
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

}
