package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;

@Mapper
public interface UserProjectRoleDao {
  // 프로젝트 생성시 UserRole 추가
  int insertUsersProjectRole(UserProjectRole userProjectRole);

  // 특정 프로젝트의 멤버 조회(여러명) selectProjectMembers
  List<UserProjectRole> selectProjectMembers(@Param("projectId") int projectId);

  // 특정 사용자가 참여한 프로젝트 조회
  List<UserProjectRole> selectUsersProject(@Param("userId") int userId);

  // countProjectMembers
  // 특정 프로젝트의 멤버수 세기 countProjectMembers
  int countProjectMembers(@Param("projectId") int projectId);

  // 특정 프로젝트의 관리자수 찾기countProjectAdmins
  int countProjectAdmins(@Param("projectId") int projectId);

  // 프로젝트 참여하는 사용자의 플젝 내 역할 조회 selectUserProjectRole
  UserProjectRole selectUserProjectRole(@Param("projectId") int projectId, @Param("userId") int userId);

  // updateUserProjectRole
 
  
  // 역할 변경(팀장위임도 포함함)
  int updateUserProjectRole(UserProjectRole userProjectRole);

  // 프로젝트에서 유저 방출(제거) deleteUserProjectRole
  int deleteUserProjectRole(@Param("projectId") int projectId, @Param("userId") int userId);

  // 프로젝트의 모든 유저 제거(프로젝트 삭제시 실행)
  int deleteAllUserProjectRole(int projectId);
}
