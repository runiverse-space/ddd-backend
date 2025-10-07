package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.users.Users;

@Mapper
public interface UsersDao {
  public int insertUser(Users users);
  public Users selectUserById(int userId);
  public Users selectUserByLoginId(String userLoginId);
  public Users selectUserByEmail(String userEmail);
  public List<Users> selectAllUsers();
  public int updateUser(Users users);
  public int deleteUser(int userId);
  public int deleteUserProfileImg(int userId);
  public List<Users> searchUsers(String keyword);
}