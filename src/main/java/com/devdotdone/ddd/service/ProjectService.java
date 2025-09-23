package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.devdotdone.ddd.dao.ProjectDao;
import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.project.Project;
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
  private UserProjectRoleDao uprDao;

  private static final int MAX_MEMBERS = 6;

  @Transactional
  public Project create(ProjectRequest request) {
    Users creator = usersDao.selectUserById(request.getUserId());
    if (creator == null) {
      throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    }

    Project project = new Project();
    project.setUserId(request.getUserId());
    project.setProjectTitle(request.getProjectTitle());
    project.setProjectContent(request.getProjectContent());
    project.setProjectStartDate(request.getProjectStartDate());
    project.setProjectEndDate(request.getProjectEndDate());

    projectDao.insertProject(project);

    UserProjectRole upr = new UserProjectRole();
    upr.setUserId(request.getUserId());
    upr.setProjectId(project.getProjectId());
    upr.setUprRole("ADMIN");
    uprDao.insertUsersProjectRole(upr);

    return projectDao.selectProjectById(project.getProjectId());
  }

  @Transactional
  public void assignUsers(int projectId, int userId, String uprRole) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null)
      throw new IllegalArgumentException("프로젝트가 존재하지 않습니다.");

    Users users = usersDao.selectUserById(userId);
    if (users == null)
      throw new IllegalArgumentException("사용자가 존재하지 않습니다.");

    UserProjectRole exist = uprDao.selectUserProjectRole(projectId, userId);
    if (exist != null)
      throw new IllegalStateException("이미 프로젝트 멤버입니다.");

    int current = uprDao.countProjectMembers(projectId);
    if (current >= MAX_MEMBERS)
      throw new IllegalStateException("프로젝트는 최대 " + MAX_MEMBERS + "명까지 참여할 수 있습니다.");

    String normalizeRole = uprRole == null ? "MEMBER" : uprRole.toUpperCase();
    if ("ADMIN".equals(normalizeRole)) {
      int adminCount = uprDao.countProjectAdmins(projectId);
      if (adminCount > 0)
        throw new IllegalStateException("이미 팀장이 존재합니다.");
    }

    UserProjectRole upr = new UserProjectRole();
    upr.setUserId(userId);
    upr.setProjectId(projectId);
    upr.setUprRole(normalizeRole);
    uprDao.insertUsersProjectRole(upr);
  }

  @Transactional
  public void removeMember(int projectId, int userId) {
    UserProjectRole exist = uprDao.selectUserProjectRole(projectId, userId);
    if (exist == null)
      throw new IllegalArgumentException("해당 사용자는 프로젝트 멤버가 아닙니다.");

    if ("ADMIN".equalsIgnoreCase(exist.getUprRole())) {
      int adminCount = uprDao.countProjectAdmins(projectId);
      if (adminCount <= 1) {
        throw new IllegalStateException("마지막 팀장은 직접 삭제할 수 없습니다. 팀장을 변경 후 삭제하세요.");
      }
    }
    uprDao.deleteUserProjectRole(projectId, userId);
  }

  public void changeRole(int projectId, int userId, String newRole) {
    UserProjectRole exist = uprDao.selectUserProjectRole(projectId, userId);
    if (exist == null)
      throw new IllegalArgumentException("해당 유저는 프로젝트 멤버가 아닙니다.");

    String normalized = newRole.toUpperCase();
    if (!"ADMIN".equals(normalized) && !"MEMBER".equals(normalized)) {
      throw new IllegalArgumentException("role은 ADMIN 또는 MEMBER 만 가능합니다.");
    }

    if ("ADMIN".equals(normalized)) {
      // 다른 ADMIN 존재하는지 확인
      int adminCount = uprDao.countProjectAdmins(projectId);
      if (adminCount > 0 && !"ADMIN".equalsIgnoreCase(exist.getUprRole())) {
        throw new IllegalStateException("이미 다른 팀장이 존재합니다.");
      }
    }

    UserProjectRole upr = new UserProjectRole();
    upr.setProjectId(projectId);
    upr.setUserId(userId);
    upr.setUprRole(normalized);
    uprDao.updateUserProjectRole(upr);
  }

  public List<UserProjectRole> getProjectMembers(int projectId) {
    return uprDao.selectProjectMembers(projectId);
  }

}
