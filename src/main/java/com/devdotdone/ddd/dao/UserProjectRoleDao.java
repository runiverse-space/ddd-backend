package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;

@Mapper
public interface UserProjectRoleDao {
  // insertUsersProjectRole
  int insertUsersProjectRole(UserProjectRole userProjectRole);

  // selectProjectMembers
  List<UserProjectRole> selectProjectMembers(@Param("projectId") int projectId);

  // 특정 사용자가 참여한 프로젝트 조회
  List<UserProjectRole> selectUsersProject(@Param("userId") int userId);

  // countProjectMembers
  int countProjectMembers(@Param("projectId") int projectId);

  // countProjectAdmins
  int countProjectAdmins(@Param("projectId") int projectId);

  // selectUserProjectRole
  UserProjectRole selectUserProjectRole(@Param("projectId") int projectId, @Param("userId") int userId);

  // updateUserProjectRole
  int updateUserProjectRole(UserProjectRole userProjectRole);

  // deleteUserProjectRole
  int deleteUserProjectRole(@Param("projectId") int projectId, @Param("userId") int userId);

  // 프로젝트의 모든 유저 제거(프로젝트 삭제시 실행)
  int deleteAllUserProjectRole(int projectId);
}
