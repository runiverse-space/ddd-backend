package com.devdotdone.ddd.controller.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.project.ProjectMilestone;
import com.devdotdone.ddd.service.ProjectMilestoneService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projectmilestone")
@RequiredArgsConstructor
public class ProjectMilestoneController implements ProjectMilestoneControllerDocs {
  private final ProjectMilestoneService projectMilestoneService;

  @PostMapping("/create")
  public Map<String, Object> create(@RequestBody ProjectMilestone projectMilestone) {
    Map<String, Object> map = new HashMap<>();
    try {
      projectMilestoneService.createMilestone(projectMilestone);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/list")
  public Map<String, Object> list(@RequestParam("projectId") int projectId) {
    Map<String, Object> map = new HashMap<>();
    try {
      List<ProjectMilestone> projectMilestones = projectMilestoneService.getMilestonesByProject(projectId);
      map.put("result", "success");
      map.put("data", projectMilestones);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }

  @GetMapping("/detail")
  public Map<String, Object> detail(@RequestParam("milestoneId") int milestoneId) {
    Map<String, Object> map = new HashMap<>();
    try {
      ProjectMilestone projectMilestone = projectMilestoneService.getMilestoneById(milestoneId);
      map.put("result", "success");
      map.put("data", projectMilestone);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }

  @PutMapping("/update")
  public Map<String, Object> update(@RequestBody ProjectMilestone milestone) {
    Map<String, Object> map = new HashMap<>();
    try {
      ProjectMilestone projectMilestone = projectMilestoneService.updateMilestone(milestone);
      map.put("result", "success");
      map.put("data", projectMilestone);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }

  @DeleteMapping("/delete")
  public Map<String, Object> delete(@RequestParam("milestoneId") int milestoneId) {
    Map<String, Object> map = new HashMap<>();

    try {
      int rows = projectMilestoneService.deleteMilestone(milestoneId);
      if (rows == 0) {
        map.put("result", "fail");
        map.put("message", "삭제 실패");
      } else {
        map.put("result", "success");
        map.put("message", "마일스톤이 삭제되었습니다.");
      }
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }

}
