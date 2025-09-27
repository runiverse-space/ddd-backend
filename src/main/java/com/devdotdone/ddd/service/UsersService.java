package com.devdotdone.ddd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.devdotdone.ddd.dao.ScheduleMemberDao;
import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.users.Users;

import ch.qos.logback.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
  private final UsersDao usersDao;
  private final UserProjectRoleDao userProjectRoleDao;

  public void insertUser(Users users) {
    usersDao.insertUser(users);
  }

  
  //식별자 ID로 조회
  public Users getUsers(int userId){
    Users users= usersDao.selectUserById(userId);
    return users;
  }

  // 로그인 ID로 조회
  public Users getUsersByLoginId(String userLoginId) {
    return usersDao.selectUserByLoginId(userLoginId);
  }

  // 이메일로 조회
  public Users getUsersByEmail(String userEmail) {
    return usersDao.selectUserByEmail(userEmail);
  }

  //useId로 사용자 정보 수정
  public Users update(Users users) {
    Users dbUsers = usersDao.selectUserById(users.getUserId());

    if (dbUsers == null) {
      return null;
    } else {
      if (StringUtils.hasText(users.getUserName())) {
        dbUsers.setUserName(users.getUserName());
      }
      if (StringUtils.hasText(users.getUserPassword())) {
        dbUsers.setUserPassword(users.getUserPassword());
      }
      if (StringUtils.hasText(users.getUserEmail())) {
        dbUsers.setUserPassword(users.getUserEmail());
      }

      dbUsers.setUfAttachoname(users.getUfAttachoname());
      dbUsers.setUfAttachsname(users.getUfAttachsname());
      dbUsers.setUfAttachtype(users.getUfAttachtype());
      dbUsers.setUfAttachdata(users.getUfAttachdata());

    }

    usersDao.updateUser(dbUsers);
    dbUsers = usersDao.selectUserById(users.getUserId());

    return dbUsers;
  }

  public int deleteUser(int userId) {
    // if (userProjectRoleDao.selectUsersProject(userId).size() > 0) {
    //   throw new Exception("프로젝트에 참여 중입니다. 팀장에게 문의하세요.");
    // }

    int rows = usersDao.deleteUser(userId);
    return rows;
  }

  public int deleteUserProfileImg(int userId) {
    int rows = usersDao.deleteUserProfileImg(userId);
    log.info("----------deleteUserProfileImg 수정");
    return rows;
  }

}
