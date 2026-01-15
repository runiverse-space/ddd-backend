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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    // 이미 프로젝트 참여중인지 확인하고 없으면 추가
    boolean isDuplicate = userProjectRoleDao.countUserProjectRole(projectId, userId)>0;

    if (isDuplicate) {
      throw new IllegalArgumentException("이미 프로젝트 멤버입니다.");
    }
    // 최대 멤버수 확인
    int current = getCountProjectMembers(projectId);
    if (current >= MAX_MEMBERS) {
      throw new IllegalStateException("프로젝트는 최대 " + MAX_MEMBERS + "명까지 참여할 수 있습니다.");
    }
    // ** 역할 정규화
    String normalizeRole = uprRole == null ? "MEMBER" : uprRole.toUpperCase();

    // ** ADMIN 역할 중복 확인
    if ("ADMIN".equals(normalizeRole)) {
      int adminCount = getCountProjectAdmins(projectId);
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
  public int deleteUsersFromProject(int projectId, int userId) {

    UserProjectRole existUser = userProjectRoleDao.selectUserProjectRole(projectId, userId);
    if (existUser == null) {
      throw new IllegalArgumentException("해당 사용자는 프로젝트의 멤버가 아닙니다");
    }
    if ("ADMIN".equalsIgnoreCase(existUser.getUprRole())) {
      int adminCount = getCountProjectAdmins(projectId);
      if (adminCount <= 1) {
        throw new IllegalStateException("팀장은 직접 삭제할 수 없습니다. 팀장을 변경 후 삭제하세요.");
      }
    }

    int rows = userProjectRoleDao.deleteUserProjectRole(projectId, userId);
    return rows;

  }

  /*
   * 역할 변경 (팀장 위임)
   */

  @Transactional
  public int updateAdmin(UserProjectRole userProjectRole){

     //1. 프로젝트 존재 여부 확인
    Project project = projectDao.selectProjectById(userProjectRole.getProjectId());
    if (project == null) {
      throw new IllegalArgumentException("프로젝트가 존재하지 않습니다.");
    }

    //2. 새로운 ADMIN이 될 사용자가 프로젝트 멤버인지 확인
    UserProjectRole newAdminRole= userProjectRoleDao.selectUserProjectRole(userProjectRole.getProjectId(), userProjectRole.getUserId());
    if(newAdminRole==null){
      throw new IllegalArgumentException("해당 사용자는 프로젝트 멤버가 아닙니다.");
    }

    //3.이미 ADMIN인 경우 처리
    if ("ADMIN".equalsIgnoreCase(newAdminRole.getUprRole())) {
        throw new IllegalStateException("해당 사용자는 이미 팀장입니다.");
    }

    // 현재 ADMIN 찾기
    List<UserProjectRole> projectMembers= userProjectRoleDao.selectProjectMembers(userProjectRole.getProjectId());
    UserProjectRole currentAdmin=null;
   
    for(UserProjectRole projectMember: projectMembers ){
      if("ADMIN".equalsIgnoreCase(projectMember.getUprRole())){
        currentAdmin= projectMember;
        break;
      }
    }
      //이 경우는 없을텐데..
      if(currentAdmin==null){
        throw new IllegalStateException("현재 팀장이 존재하지 않습니다.");
      }

    //기존 Admin을 멤버로 바꿀수 있다. 
    UserProjectRole oldAdmin = new UserProjectRole();
    oldAdmin.setUserId(currentAdmin.getUserId());
    oldAdmin.setProjectId(userProjectRole.getProjectId());
    oldAdmin.setUprRole("MEMBER");
    int oldAdminResult =userProjectRoleDao.updateUserProjectRole(oldAdmin);
   
    
    //일반 멤버-> ADMIN으로 위임
    UserProjectRole newAdmin = new UserProjectRole();
    newAdmin.setUserId(userProjectRole.getUserId());
    newAdmin.setProjectId(userProjectRole.getProjectId());
    newAdmin.setUprRole("ADMIN");
    //다른 사용자가 admin까지 되면 위임 완료 ,updateAdmin 완료
    int newAdminResult=userProjectRoleDao.updateUserProjectRole(newAdmin);

    //결과 확인
    if(oldAdminResult<=0|| newAdminResult<=0){
      throw new IllegalStateException("팀장 위임에 실패하였습니다.");
    }
    return newAdminResult;
  }

  /*
   * 프로젝트 멤버 목록 조회
   */

  public List<UserProjectRole> getProjectMember(int projectId) {
    // 프로젝트 존재 여부 확인
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }
    List<UserProjectRole> uprList = userProjectRoleDao.selectProjectMembers(projectId);
    log.info("프로젝트에 속한 멤버: {}", uprList);
     log.info("멤버 수: {}", uprList.size());

    return uprList;
  }

  /*
    특정 유저가 속한 프로젝트 목록 조회
   */
  public List<UserProjectRole> getProjectsListByUserId(int userId){
    //유저 존재 확인
    Users users = usersDao.selectUserById(userId);
    if(users ==null){
      throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    }

    return userProjectRoleDao.selectUsersProject(userId);
  }

  /*
    특정 유저가 속한 프로젝트 목록 조회
   */
 


  /*
   * 사용자의 특정 프로젝트 역할 조회(admin인지 일반 member인지 구별)
   */

  public String getUserProjectRole(int projectId, int userId){
   UserProjectRole userRole= userProjectRoleDao.selectUserProjectRole(projectId, userId);
    
   if(userRole!=null && userRole.getUprRole()!=null && !userRole.getUprRole().trim().isEmpty()){
    return userRole.getUprRole().trim();
   }else{
    return "NONE";
   }
   

  }

  /*
   * admin 찾기
   */

  public int getProjectAdmins(int projectId){
    UserProjectRole userRole= userProjectRoleDao.selectProjectAdmins(projectId);
  
     if (userRole == null) {
        return 0;
        //** admin이 없으면 null 반환
    }

    int userId= userRole.getUserId();
    return userId;
  
  }

  

  /*
   * 프로젝트 멤버 수 조회
   */
  public int getCountProjectMembers(int projectId){
    return userProjectRoleDao.countProjectMembers(projectId);
  }

  /*
   * 프로젝트 ADMIN 수 조회
   */
  public int getCountProjectAdmins(int projectId){
    return userProjectRoleDao.countProjectAdmins(projectId);
  }

  
}

