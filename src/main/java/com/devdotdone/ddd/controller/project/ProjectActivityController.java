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
  public Map<String, Object> participate(@RequestBody ProjectActivity activity) {
    log.info("participate()");
    Map<String, Object> map = new HashMap<>();
    try {
      Project project = projectService.getProjectById(activity.getProjectId());
      int receiverId = project.getUserId();
      projectActivityService.sendParticipation(project, activity.getSenderId(), receiverId);
      ProjectActivity dbActivity = projectActivityService.getById(activity.getActivityId());
      map.put("result", "success");
      map.put("data", dbActivity);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  // 참여 허가 후 paStatus를 "APPROVED"로 변경, paIsRead를 "Y"로 변경
  @PutMapping("/approve-participation")
  public Map<String, Object> approveParticipation(@RequestBody ProjectActivity activity) {
    log.info("approveParticipation()");
    Map<String, Object> map = new HashMap<>();
    ProjectActivity dbActivity = projectActivityService.getById(activity.getActivityId());
    try {
      dbActivity.setPaStatus("APPROVED");
      log.info("정보 기입 완료");
      projectActivityService.updateActivityStatus(dbActivity);
      log.info("수정 실행 완료");
      dbActivity = projectActivityService.getById(activity.getActivityId());
      map.put("result", "success");
      map.put("data", dbActivity);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
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
