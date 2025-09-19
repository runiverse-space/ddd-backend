package com.devdotdone.ddd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.users.Users;

@Service
public class UsersService {
  @Autowired
  private UsersDao usersDao;

  public void insertUser(Users users) {
    usersDao.insertUser(users);
  }
}
