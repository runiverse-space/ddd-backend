package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.ProjectDao;
import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;
import com.devdotdone.ddd.dto.users.Users;

@Service
public class UserProjectRoleService {

  @Autowired
  private UserProjectRoleDao userProjectRoleDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private UsersDao usersDao;

  private static final int MAX_MEMBERS = 6;

  /*
   * 프로젝트 생성자를 admin으로 등록 ( mapper에서 안하고 service에서 admin 만들기로했음)
   * project를 만들때 이쪽으로 projectId, userId를 보내줄것임
   */
  @Transactional
  public void assignUsersProjectAdmin(int projectId, int userId) {

    //
    UserProjectRole userProjectRole = new UserProjectRole();
    userProjectRole.setUserId(userId);
    userProjectRole.setProjectId(projectId);
    userProjectRole.setUprRole("ADMIN");

    int rows = userProjectRoleDao.insertUsersProjectRole(userProjectRole);
    if (rows <= 0) {
      throw new RuntimeException("프로젝트 생성자 등록에 실패했습니다.");
    }

  }

  /*
   * 프로젝트에서 사용자 할당
   */
  @Transactional
  public void assignUsersToProject(int projectId, int userId, String uprRole) {

    // 프로젝트 존재 여부 확인
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("프로젝트가 존재하지 않습니다.");
    }

    // 사용자 존재 여부 확인
    Users users = usersDao.selectUserById(userId);
    if (users == null) {
      throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
    }

    // 이미 프로젝트 참여중인지 확인
    UserProjectRole existUpr = userProjectRoleDao.selectUserProjectRole(projectId, userId);

    if (existUpr != null) {
      throw new IllegalArgumentException("이미 프로젝트 멤버입니다.");
    }
    //최대 멤버수 확인
    int current = userProjectRoleDao.countProjectMembers(projectId);
    if (current >= MAX_MEMBERS) {
      throw new IllegalStateException("프로젝트는 최대 " + MAX_MEMBERS + "명까지 참여할 수 있습니다.");
    }
    //** 역할 정규화
    String normalizeRole = uprRole == null ? "MEMBER" : uprRole.toUpperCase();
    
    //** ADMIN 역할 중복 확인
    if ("ADMIN".equals(normalizeRole)) {
        int adminCount = userProjectRoleDao.countProjectAdmins(projectId);
        if (adminCount > 0) {
            throw new IllegalStateException("이미 팀장이 존재합니다.");
        }
    }


    UserProjectRole createMember = new UserProjectRole();
    createMember.setProjectId(projectId);
    createMember.setUserId(userId);
    createMember.setUprId(userId);
    createMember.setUprRole(uprRole);
    userProjectRoleDao.insertUsersProjectRole(createMember);

  }

  /*
   * 프로젝트에서 멤버 제거
   */
  @Transactional
  public int delete(int projectId, int userId) {

    UserProjectRole existUser= userProjectRoleDao.selectUserProjectRole(projectId, userId);
    if(existUser==null){
      throw new IllegalArgumentException("해당 사용자는 프로젝트의 멤버가 아닙니다");
    }
    if ("ADMIN".equalsIgnoreCase(existUser.getUprRole())) {
        int adminCount = userProjectRoleDao.countProjectAdmins(projectId);
        if (adminCount <= 1) {
            throw new IllegalStateException("마지막 팀장은 직접 삭제할 수 없습니다. 팀장을 변경 후 삭제하세요.");
        }
    }

    int rows = userProjectRoleDao.deleteUserProjectRole(projectId, userId);
    return rows;

  }

  /*
   * 사용자 역할 변경
   */
  @Transactional
  public int update(UserProjectRole userProjectRole) {

    // 현재 사용자의 프로젝트 역할조회 userprojectrole을 통해서
    UserProjectRole nowUpr = userProjectRoleDao.selectUserProjectRole(userProjectRole.getProjectId(),userProjectRole.getUserId());

    // 해당 프로젝트 멤버가 아닐때 projectId
    if (nowUpr == null) {
      throw new IllegalArgumentException("해당 유저는 프로젝트 멤버가 아닙니다");
    }

    // 새롭게 바뀐 admin이나 멤버
    String changeUpr = userProjectRole.getUprRole();

    // admin으로 변경하는 경우 countProjectAdmins해서 중복확인 (프론트에서 셀렉트 option으로 멤버랑 admin 둘중
    // 하나만 선택하게 만들면? 둘중 하나만 들어오나..)
    if ("ADMIN".equals(changeUpr)) {
      int adminCount = userProjectRoleDao.countProjectAdmins(userProjectRole.getProjectId());

      // 이미 다른 admin이 존재하고 현재 사용자가 admin이 아닌 경우
      if (adminCount > 0 && !"ADMIN".equalsIgnoreCase(nowUpr.getUprRole())) {
        throw new IllegalStateException("이미 다른 팀장이 존재합니다.");
      }
    }

    /*
     * UserProjectRole updateUpr= new UserProjectRole() ;
     * updateUpr.setProjectId(userProjectRole.getProjectId());
     * updateUpr.setUserId(userProjectRole.getUserId());
     * updateUpr.setUprRole(changeUpr);
     */
    int rows = userProjectRoleDao.updateUserProjectRole(userProjectRole);
    return rows;
  }

  /*
   * 프로젝트 멤버 목록 조회
   */
  public List<UserProjectRole> getUsersProject(int projectId){
   //프로젝트 존재 여부 확인
    Project project= projectDao.selectProjectById(projectId);
    if(project==null){
       throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }
    List<UserProjectRole> uprList = userProjectRoleDao.selectProjectMembers(projectId);
    return uprList;
  }

  /*
   * 사용자의 특정 프로젝트 역할 조회
   */

  /*
   * 프로젝트 멤버 수 조회
   */

  /*
   * 프로젝트 ADMIN 수 조회
   */
}
