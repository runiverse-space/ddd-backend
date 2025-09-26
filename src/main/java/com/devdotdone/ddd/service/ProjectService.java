package com.devdotdone.ddd.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.ProjectDao;
import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectMilestone;
import com.devdotdone.ddd.dto.project.ProjectRequest;
import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;
import com.devdotdone.ddd.dto.users.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectDao projectDao;
  private final UsersDao usersDao;
  private final UserProjectRoleDao userProjectRoleDao;
  private final UserProjectRoleService userProjectRoleService;
  private final ProjectMilestoneService projectMilestoneService;

  private static final int MAX_MEMBERS = 6;

  // 새 프로젝트 만들기
  @Transactional
  public Project create(ProjectRequest request) {
    // 생성자 존재 여부 확인
    Users creator = usersDao.selectUserById(request.getUserId());
    if (creator == null) {
      throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    }

    // 프로젝트 생성
    Project project = new Project();
    project.setUserId(request.getUserId());
    project.setProjectTitle(request.getProjectTitle());
    project.setProjectContent(request.getProjectContent());
    project.setProjectStartDate(request.getProjectStartDate());
    project.setProjectEndDate(request.getProjectEndDate());

    int result = projectDao.insertProject(project);
    // System.out.println("result:" + result);
    log.info(project.toString());
    if (result <= 0) {
      throw new RuntimeException("프로젝트 생성에 실패했습니다.");
    }

    // 생성자를 ADMIN으로 할당 (다른 서비스에 위임)
    userProjectRoleService.assignUsersProjectAdmin(project.getProjectId(), request.getUserId());

    // 사용자 MEMBER로 추가
    for (int userId : request.getUserIds()) {
      assignUsers(project.getProjectId(), userId, "MEMBER");
    }

    // 초기 마일스톤 추가
    for (ProjectMilestone milestone : request.getProjectMilestones()) {
      milestone.setProjectId(project.getProjectId());
      projectMilestoneService.createMilestone(milestone);
    }

    return projectDao.selectProjectById(project.getProjectId());
  }

  // 프로젝트에 멤버 추가
  @Transactional
  public void assignUsers(int projectId, int userId, String uprRole) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null)
      throw new IllegalArgumentException("프로젝트가 존재하지 않습니다.");

    Users users = usersDao.selectUserById(userId);
    if (users == null)
      throw new IllegalArgumentException("사용자가 존재하지 않습니다.");

    UserProjectRole exist = userProjectRoleDao.selectUserProjectRole(projectId, userId);
    if (exist != null)
      throw new IllegalStateException("이미 프로젝트 멤버입니다.");

    int current = userProjectRoleDao.countProjectMembers(projectId);
    if (current >= MAX_MEMBERS)
      throw new IllegalStateException("프로젝트는 최대 " + MAX_MEMBERS + "명까지 참여할 수 있습니다.");

    String normalizeRole = uprRole == null ? "MEMBER" : uprRole.toUpperCase();
    if ("ADMIN".equals(normalizeRole)) {
      int adminCount = userProjectRoleDao.countProjectAdmins(projectId);
      if (adminCount > 0)
        throw new IllegalStateException("이미 팀장이 존재합니다.");
    }

    UserProjectRole upr = new UserProjectRole();
    upr.setUserId(userId);
    upr.setProjectId(projectId);
    upr.setUprRole(normalizeRole);
    userProjectRoleDao.insertUsersProjectRole(upr);
  }

  // 프로젝트 단건 조회
  public Project getProjectById(int projectId) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }
    return project;
  }

  // 프로젝트 수정
  @Transactional
  public Project update(ProjectRequest request) {
    // 기존 프로젝트 존재 여부 확인
    Project project = projectDao.selectProjectById(request.getProjectId());
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }

    // 프로젝트 제목 검증
    if (project.getProjectTitle() != null && project.getProjectTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("프로젝트 제목은 빈 값일 수 없습니다.");
    }

    project.setProjectTitle(request.getProjectTitle());
    project.setProjectContent(request.getProjectContent());
    project.setProjectStartDate(request.getProjectStartDate());
    project.setProjectEndDate(request.getProjectEndDate());

    projectDao.updateProject(project);

    int result = projectDao.updateProject(project);
    if (result <= 0) {
      throw new RuntimeException("프로젝트 수정에 실패했습니다.");
    }

    return projectDao.selectProjectById(project.getProjectId());
  }

  @Transactional
  public int delete(int projectId) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }

    // ** 관련 데이터 삭제 (다른 서비스들에 위임)
    projectMilestoneService.deleteAllMilestonesByProject(projectId);
    // ** UserProjectRole은 CASCADE DELETE나 별도 삭제 로직 필요
    userProjectRoleDao.deleteAllUserProjectRole(projectId);

    int result = projectDao.deleteProject(projectId);
    if (result <= 0) {
      throw new RuntimeException("프로젝트 삭제에 실패했습니다.");
    }

    log.info("프로젝트 {}가 삭제되었습니다.", projectId);
    return result;
  }

  // @Transactional
  // public void removeMember(int projectId, int userId) {
  // UserProjectRole exist = uprDao.selectUserProjectRole(projectId, userId);
  // if (exist == null)
  // throw new IllegalArgumentException("해당 사용자는 프로젝트 멤버가 아닙니다.");

  // if ("ADMIN".equalsIgnoreCase(exist.getUprRole())) {
  // int adminCount = uprDao.countProjectAdmins(projectId);
  // if (adminCount <= 1) {
  // throw new IllegalStateException("마지막 팀장은 직접 삭제할 수 없습니다. 팀장을 변경 후 삭제하세요.");
  // }
  // }
  // uprDao.deleteUserProjectRole(projectId, userId);
  // }

  // public void changeRole(int projectId, int userId, String newRole) {
  // UserProjectRole exist = uprDao.selectUserProjectRole(projectId, userId);
  // if (exist == null)
  // throw new IllegalArgumentException("해당 유저는 프로젝트 멤버가 아닙니다.");

  // String normalized = newRole.toUpperCase();
  // if (!"ADMIN".equals(normalized) && !"MEMBER".equals(normalized)) {
  // throw new IllegalArgumentException("role은 ADMIN 또는 MEMBER 만 가능합니다.");
  // }

  // if ("ADMIN".equals(normalized)) {
  // // 다른 ADMIN 존재하는지 확인
  // int adminCount = uprDao.countProjectAdmins(projectId);
  // if (adminCount > 0 && !"ADMIN".equalsIgnoreCase(exist.getUprRole())) {
  // throw new IllegalStateException("이미 다른 팀장이 존재합니다.");
  // }
  // }

  // UserProjectRole upr = new UserProjectRole();
  // upr.setProjectId(projectId);
  // upr.setUserId(userId);
  // upr.setUprRole(normalized);
  // uprDao.updateUserProjectRole(upr);
  // }

  // public List<UserProjectRole> getProjectMembers(int projectId) {
  // return uprDao.selectProjectMembers(projectId);
  // }

}
