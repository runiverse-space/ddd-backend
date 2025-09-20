package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.users.UserProjectRole;

@Mapper
public interface UserProjectRoleDao {
  public int insertUsersProjectRoleAsAdmin(UserProjectRole userProjectRole);
  public int insertUsersProjectRoleAsMember(UserProjectRole userProjectRole);
  //특정 프로젝트 멤버 조회
  public List<UserProjectRole> selectUsersByProjectId(int projectId);
  //특정 유저의 프로젝트 목록 조회
  public List<UserProjectRole> selectUsersByUserId(int userId);
  //유저 권한 확인
  public String selectUserRoleInProject(UserProjectRole userProjectRole);
  //프로젝트에서 유저 제거
  public int deleteUserFromProject(@Param("userId")int userId,@Param("projectId") int projectId);

}
