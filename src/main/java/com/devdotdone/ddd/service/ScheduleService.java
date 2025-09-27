package com.devdotdone.ddd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.ScheduleDao;
import com.devdotdone.ddd.dao.ScheduleMemberDao;
import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.schedule.Schedule;
import com.devdotdone.ddd.dto.schedule.ScheduleMember;
import com.devdotdone.ddd.dto.schedule.ScheduleRequest;
import com.devdotdone.ddd.dto.users.Users;
import com.devdotdone.ddd.dto.users.UsersResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduleService {
  @Autowired
  private ScheduleDao scheduleDao;
  
  @Autowired
  private ScheduleMemberDao scheduleMemberDao;
  
  @Autowired
  private UsersDao usersDao;
  
  @Autowired
  private UserProjectRoleDao userProjectRoleDao;

  // 일정 추가
  @Transactional
  public void create(ScheduleRequest request) {
    Schedule schedule = new Schedule();

    schedule.setProjectId(request.getProjectId());
    schedule.setUserId(request.getUserId());
    schedule.setScheduleTitle(request.getScheduleTitle());
    schedule.setScheduleContent(request.getScheduleContent());
    schedule.setScheduleStartDate(request.getScheduleStartDate());
    schedule.setScheduleEndDate(request.getScheduleEndDate());
    schedule.setScheduleStatus(request.getScheduleStatus());

    log.info(schedule.toString());
    scheduleDao.insertSchedule(schedule);

    assignUsers(schedule.getScheduleId(), schedule.getProjectId(), request.getUserIds());
  }

  // 개별 일정 조회
  public Schedule getSchedule(int scheduleId) {
    return scheduleDao.selectScheduleById(scheduleId);
  }

  // 프로젝트에 속한 일정 조회
  public List<Schedule> getListByProject(int projectId) {
    return scheduleDao.selectScheduleByProjectId(projectId);
  }

  // 일정을 배정받은 사용자 조회
  public List<UsersResponse> getAssignedUsers(int scheduleId) {
    List<UsersResponse> usersResponseList = new ArrayList<>();
    List<ScheduleMember> smList = scheduleMemberDao.findUsers(scheduleId);
    for (ScheduleMember sm : smList) {
      int userId = sm.getUserId();
      Users users = usersDao.selectUserById(userId);

      UsersResponse usersResponse = new UsersResponse();

      usersResponse.setUserId(users.getUserId());
      usersResponse.setUserLoginId(users.getUserLoginId());
      usersResponse.setUserName(users.getUserName());
      usersResponse.setUserEmail(users.getUserEmail());
      usersResponse.setUserIntro(users.getUserIntro());
      usersResponse.setUfAttach(users.getUfAttach());
      usersResponse.setUfAttachoname(users.getUfAttachoname());
      usersResponse.setUfAttachtype(users.getUfAttachtype());
      usersResponse.setUfAttachdata(users.getUfAttachdata());

      usersResponseList.add(usersResponse);
    }
    return usersResponseList;
  }

  // 일정 수정
  @Transactional
  public Schedule update(ScheduleRequest request) {
    Schedule schedule = scheduleDao.selectScheduleById(request.getScheduleId());
    if (schedule == null) {
      return null;
    }
    // schedule.setProjectId(request.getProjectId());
    log.info("수정전: {}", schedule);
    schedule.setUserId(request.getUserId());
    schedule.setScheduleTitle(request.getScheduleTitle());
    schedule.setScheduleContent(request.getScheduleContent());
    schedule.setScheduleStartDate(request.getScheduleStartDate());
    schedule.setScheduleEndDate(request.getScheduleEndDate());
    schedule.setScheduleStatus(request.getScheduleStatus());

    log.info(schedule.toString());
    scheduleDao.updateSchedule(schedule);

    log.info("업데이트됨");

    assignUsers(schedule.getScheduleId(), schedule.getProjectId(), request.getUserIds());
    cancelUsers(schedule.getScheduleId(), request.getUserIds());

    return schedule;
  }

  // 일정 삭제
  public int remove(int scheduleId) {
    scheduleMemberDao.dischargeAll(scheduleId);
    return scheduleDao.deleteSchedule(scheduleId);
  }

  // 일정 할당
  private void assignUsers(int scheduleId, int projectId, List<Integer> userIds) {
    for (int userId : userIds) {
      if (usersDao.selectUserById(userId) == null) {
        throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
      }
      if (userProjectRoleDao.selectUserProjectRole(projectId, userId) == null) {
        throw new IllegalArgumentException(userId + "는 프로젝트 " + projectId + "의 구성원이 아닙니다.");
      }
      
      ScheduleMember sm = new ScheduleMember();
      sm.setScheduleId(scheduleId);
      sm.setUserId(userId);

      ScheduleMember dbsm = scheduleMemberDao.selectOne(sm);
      if (dbsm == null)
        scheduleMemberDao.chargeUsers(sm);
      
    }
  }

  // 일정 할당 해제
  private void cancelUsers(int scheduleId, List<Integer> userIds) {
    List<ScheduleMember> smList = scheduleMemberDao.findUsers(scheduleId);
    
    for (ScheduleMember sm: smList) {
      if (!userIds.contains(sm.getUserId())) {
        scheduleMemberDao.dischargeUsers(sm);
      }
    }
  }
}
