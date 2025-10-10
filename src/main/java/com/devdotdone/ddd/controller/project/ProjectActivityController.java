package com.devdotdone.ddd.controller.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectActivity;
import com.devdotdone.ddd.service.ProjectActivityService;
import com.devdotdone.ddd.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/project-activity")
@Slf4j
public class ProjectActivityController {
  @Autowired
  private ProjectActivityService projectActivityService;
  
  @Autowired
  private ProjectService projectService;

  // 유저별 SSE 연결 생성
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(@RequestParam int userId) {
    return projectActivityService.subscribe(userId);
  }

  // 프로젝트 참여 요청 발생 시 알림 전송
  @PostMapping("/participate")
  public ResponseEntity<String> participate(@RequestParam("projectId") int projectId, @RequestParam("senderId") int senderId) {
    log.info("participate()");
    Project project = projectService.getProjectById(projectId);
    int receiverId = project.getUserId();
    projectActivityService.sendParticipation(project, senderId, receiverId);
    return ResponseEntity.ok("Participation request sent to " + receiverId + ".");
  }

  // 일정 배정 발생 시 알림 전송
  @PostMapping("/schedule-assignment")
  public ResponseEntity<String> scheduleAssignment(@RequestParam("scheduleId") int scheduleId,
      @RequestParam("projectId") int projectId,
      @RequestParam("senderId") int senderId,
      @RequestBody List<Integer> userIds) {
    projectActivityService.sendAssignmentsToMultipleUsers(scheduleId, projectId, senderId, userIds);
    return ResponseEntity.ok("Invitations sent to " + userIds.size() + " users.");
  }

  // 알림 목록 조회
  @GetMapping("/list")
  public Map<String, Object> list(@RequestParam("receiverId") int receiverId) {
    Map<String, Object> map = new HashMap<>();
    try {
      List<ProjectActivity> activities = projectActivityService.getByReceiverId(receiverId);
      map.put("result", "success");
      map.put("data", activities);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }
}
