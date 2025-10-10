package com.devdotdone.ddd.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.devdotdone.ddd.dao.ProjectActivityDao;
import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectActivity;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectActivityService {

  @Autowired
  private ProjectActivityDao projectActivityDao;

  // 사용자별 SseEmitter 저장소
  private final Map<Integer, SseEmitter> emitters = new java.util.concurrent.ConcurrentHashMap<>();

  // 연결 유지 시간: 3일(연결 유지 시간이 만료되면 더이상 알림을 받을 수 없음)
  private static final long TIMEOUT = 3 * 24 * 60 * 60 * 1000;

  // 사용자별 SSE 연결 생성
  public SseEmitter subscribe(int userId) {
    SseEmitter emitter = new SseEmitter(TIMEOUT);
    emitters.put(userId, emitter); // 키: userId, 값: emitter 객체

    emitter.onCompletion(() -> emitters.remove(userId)); // 연결 종료 시 맵에서 제거
    emitter.onTimeout(() -> emitters.remove(userId)); // 타임아웃 시 맵에서 제거

    // 연결 직후 확인용 메시지 전송
    try {
      emitter.send(SseEmitter.event()
          .name("connect")
          .data("Connected to notification stream"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    log.info(emitter.toString());
    return emitter;
  }

  // 프로젝트 참여 요청 알림 전송
  public void sendParticipation(Project project, int senderId, int receiverId) {
    SseEmitter emitter = emitters.get(receiverId);

    // DB에 알림 저장
    ProjectActivity projectActivity = new ProjectActivity();

    projectActivity.setPaType("JOIN_REQUEST");
    projectActivity.setProjectId(project.getProjectId());
    projectActivity.setPaMessage(senderId + " 님이 " + project.getProjectTitle() + "에 함께하고 싶어 합니다.");
    projectActivity.setPaStatus("PENDING");
    projectActivity.setPaIsRead("N");
    projectActivity.setSenderId(senderId);
    projectActivity.setReceiverId(receiverId);

    log.info(projectActivity.toString());

    projectActivityDao.insertActivity(projectActivity);

    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event()
            .name("project-participation")
            .data(projectActivity.getPaMessage()));
        log.info("Received participation request from user {} for project {}", senderId, project.getProjectId());

      } catch (IOException e) {
        emitters.remove(receiverId); // 전송 실패 시 맵에서 제거
        log.info("Failed to send participation to user {}: {}", receiverId, e.getMessage());
      }
    } else {
      log.info("No active connection for user {}", receiverId);
    }
  }

  // 참여 요청에 대한 답변
  public void respondParticipation(Project project, int senderId, int receiverId, String paStatus) {
    SseEmitter emitter = emitters.get(receiverId);

    // DB에 알림 저장
    ProjectActivity projectActivity = new ProjectActivity();

    projectActivity.setPaType("SYSTEM_NOTIFICATION");
    projectActivity.setProjectId(project.getProjectId());

    if (paStatus.equals("APPROVED")) {
      projectActivity.setPaMessage(project.getProjectTitle() + " 참여가 승인되었습니다. 환영합니다!");
    } else {
      projectActivity.setPaMessage(project.getProjectTitle() + "에 대한 참여 요청이 거절되었습니다.");
    }
    projectActivity.setPaStatus(paStatus);
    projectActivity.setPaIsRead("N");
    projectActivity.setSenderId(senderId);
    projectActivity.setReceiverId(receiverId);

    log.info(projectActivity.toString());

    projectActivityDao.insertActivity(projectActivity);

    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event()
            .name(paStatus.toLowerCase() + "-participation")
            .data(projectActivity.getPaMessage()));
        log.info("Received participation response from user {} for project {}", senderId, project.getProjectId());

      } catch (IOException e) {
        emitters.remove(receiverId); // 전송 실패 시 맵에서 제거
        log.info("Failed to send response to user {}: {}", receiverId, e.getMessage());
      }
    } else {
      log.info("No active connection for user {}", receiverId);
    }
  }

  // 여러 사용자에게 프로젝트 초대 알림 전송
  // public void sendInvitationsToMultipleUsers(int projectId, List<Integer>
  // userIds) {
  // for (int userId : userIds) {
  // sendInvitation(userId, projectId);
  // }
  // }

  // 일정 배정 알림 전송
  public void sendAssignment(int scheduleId, int projectId, int senderId, int receiverId) {
    SseEmitter emitter = emitters.get(receiverId);
    log.info("scheduleId: {}", scheduleId);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event()
            .name("schedule-assignment")
            .data("You have been assigned to schedule ID: " + scheduleId));
        log.info("Sent assignment to user {} for schedule {}", receiverId, scheduleId);

        // DB에 알림 저장
        ProjectActivity projectActivity = new ProjectActivity();

        projectActivity.setPaType("SCHEDULE_ASSIGNMENT");
        projectActivity.setProjectId(projectId);
        projectActivity.setPaMessage("You have been assigned to schedule ID: " + scheduleId);
        projectActivity.setPaStatus("PENDING");
        projectActivity.setPaIsRead("N");
        projectActivity.setSenderId(senderId);
        projectActivity.setReceiverId(receiverId);

        log.info(projectActivity.toString());

        projectActivityDao.insertActivity(projectActivity);

      } catch (IOException e) {
        emitters.remove(receiverId); // 전송 실패 시 맵에서 제거
        log.info("Failed to send assignment to user {}: {}", receiverId, e.getMessage());
      }
    } else {
      log.info("No active connection for user {}", receiverId);
    }
  }

  // 여러 사용자에게 일정 배정 알림 전송
  @Transactional
  public void sendAssignmentsToMultipleUsers(int scheduleId, int projectId, int senderId, List<Integer> userIds) {
    for (int receiverId : userIds) {
      sendAssignment(scheduleId, projectId, senderId, receiverId);
    }
  }

  // 단건 조회
  public ProjectActivity getById(int activityId) {
    return projectActivityDao.selectById(activityId);
  }

  // 알림 목록 조회
  public List<ProjectActivity> getByReceiverId(int receiverId) {
    return projectActivityDao.selectByReceiverId(receiverId);
  }

  // 알림 상태 수정 
  public ProjectActivity updateActivityStatus(ProjectActivity activity) {
    int row = projectActivityDao.updateActivityStatus(activity);
    log.info("수정된 행 수{}", row);
    ProjectActivity dbActivity = projectActivityDao.selectById(activity.getActivityId());
    return dbActivity;
  }
}
