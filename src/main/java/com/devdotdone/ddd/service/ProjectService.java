package com.devdotdone.ddd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectService {
  @Autowired
  private ProjectDao projectDao;

  @Autowired
  private UsersDao usersDao;

  @Autowired
  private UserProjectRoleDao userProjectRoleDao;

  @Autowired
  private UserProjectRoleService userProjectRoleService;

  @Autowired
  private ProjectMilestoneService projectMilestoneService;

  private static final int MAX_MEMBERS = 6;

  // 새 프로젝트 만들기
  @Transactional
  public Project create(ProjectRequest request) {
    // 생성자 존재 여부 확인
    // userdao없이 request.getUserId가 가능한건지 확인 (주말 확인)
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
      userProjectRoleService.assignUsersToProject(project.getProjectId(), userId, "MEMBER");
    }

    // 초기 마일스톤 추가
    // for (ProjectMilestone milestone : request.getProjectMilestones()) {
    // milestone.setProjectId(project.getProjectId());
    // projectMilestoneService.createMilestone(milestone);
    // }

    return projectDao.selectProjectById(project.getProjectId());
  }

  // 프로젝트 단건 조회
  public Project getProjectById(int projectId) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }
    return project;
  }

  // 프로젝트 전부 조회
  public List<Project> getAllProjects() {
    return projectDao.selectAllProjects();
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

    // 참여자 추가
    for (int userId : request.getUserIds()) {
      userProjectRoleService.assignUsersToProject(project.getProjectId(), userId, "MEMBER");
    }

    // 참여자 삭제
    List<UserProjectRole> userProjectRoles = userProjectRoleDao.selectProjectMembers(request.getProjectId());
    log.info(userProjectRoles.toString());
    for (UserProjectRole userProjectRole : userProjectRoles) {
      int userId = userProjectRole.getUserId();
      if (!userProjectRole.getUprRole().equals("ADMIN") && !request.getUserIds().contains(userId))
        userProjectRoleService.deleteUsersFromProject(project.getProjectId(), userId);
    }

    // 마일스톤 정보 수정
    for (ProjectMilestone projectMilestone : request.getProjectMilestones()) {
      projectMilestoneService.updateMilestone(projectMilestone);
    }

    return projectDao.selectProjectById(project.getProjectId());
  }

  @Transactional
  public int remove(int projectId) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }

    // 프로젝트의 모든 마일스톤 삭제
    projectMilestoneService.deleteAllMilestonesByProject(projectId);
    // 프로젝트의 모든 멤버 역할 삭제
    userProjectRoleDao.deleteAllUserProjectRole(projectId);

    int result = projectDao.deleteProject(projectId);
    if (result <= 0) {
      throw new RuntimeException("프로젝트 삭제에 실패했습니다.");
    }

    log.info("프로젝트 {}가 삭제되었습니다.", projectId);
    return result;
  }

  public List<Project> getProjectsByUserId(int userId) {
    List<UserProjectRole> userProjects = userProjectRoleService.getProjectsListByUserId(userId);

    List<Project> projects = new ArrayList<>();
    for (UserProjectRole upr : userProjects) {
      Project project = projectDao.selectProjectById(upr.getProjectId());
      if (project != null) {
        projects.add(project);
      }
    }
    return projects;
  }

}
