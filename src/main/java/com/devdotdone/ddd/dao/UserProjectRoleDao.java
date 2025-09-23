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

  // countProjectMembers
  int countProjectMembers(@Param("projectId") int projectId);

  // countProjectAdmins
  int countProjectAdmins(@Param("projectId") int projectId);

  // selectUserProjectRole
  UserProjectRole selecUserProjectRole(@Param("projectId") int projectId, @Param("userId") int userId);

  // updateUserProjectRole
  int updateUserProjectRole(UserProjectRole userProjectRole);

  // deleteUserProjectRole
  int deleteUserProjectRole(@Param("projectId") int projectId, @Param("userId") int userId);
}
