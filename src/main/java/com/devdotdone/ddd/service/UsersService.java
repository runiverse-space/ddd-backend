package com.devdotdone.ddd.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.tag.UserTagRequest;
import com.devdotdone.ddd.dto.users.Users;
import com.devdotdone.ddd.dto.users.UsersSignupRequest;
import com.devdotdone.ddd.dto.users.UsersUpdateRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsersService {
  @Autowired
  private UsersDao usersDao;

  @Autowired
  private UserProjectRoleDao userProjectRoleDao;

  @Autowired
  private UserTagService userTagService;

  @Transactional
  public void create(UsersSignupRequest request) throws IOException {
    Users users = request.getUser();

    // 프로필 사진
    MultipartFile mf = users.getUfAttach();
    if (mf != null && !mf.isEmpty()) {
      users.setUfAttachoname(mf.getOriginalFilename());
      users.setUfAttachtype(mf.getContentType());
      users.setUfAttachdata(mf.getBytes());
    }

    // 비밀번호 암호화
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    users.setUserPassword(encoder.encode(users.getUserPassword()));

    // 저장
    usersDao.insertUser(users);

    // 태그 등록
    if (request.getTagIds() == null || request.getTagIds().isEmpty()) {
      throw new IllegalArgumentException("태그는 최소 1개 이상 선택해야 합니다.");
    }

    UserTagRequest tagRequest = new UserTagRequest();
    tagRequest.setUserId(users.getUserId());
    tagRequest.setTagIds(request.getTagIds());
    userTagService.update(tagRequest);
  }

  public Users getUsers(int userId) {
    Users users = usersDao.selectUserById(userId);
    if (users == null) {
      throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
    }
    return users;
  }

  public Users getUsersByLoginId(String loginId) {
    Users users = usersDao.selectUserByLoginId(loginId);
    if (users == null) {
      throw new IllegalArgumentException("해당 아이디가 존재하지 않습니다.");
    }
    return users;
  }

  public Users getUsersByEmail(String email) {
    Users users = usersDao.selectUserByEmail(email);
    if (users == null) {
      throw new IllegalArgumentException("해당 이메일이 존재하지 않습니다.");
    }
    return users;
  }

  public Users updateProfile(UsersUpdateRequest request) throws IOException {
    Users dbUsers = usersDao.selectUserById(request.getUserId());
    if (dbUsers == null) {
      throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
    }
    if (StringUtils.hasText(request.getUserEmail()))
      dbUsers.setUserEmail(request.getUserEmail());
    if (StringUtils.hasText(request.getUserPassword())) {
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      dbUsers.setUserPassword(encoder.encode(request.getUserPassword()));
    }
    if (StringUtils.hasText(request.getUserIntro()))
      dbUsers.setUserIntro(request.getUserIntro());

    MultipartFile mf = request.getUfAttach();
    if (mf != null && !mf.isEmpty()) {
      dbUsers.setUfAttachoname(mf.getOriginalFilename());
      dbUsers.setUfAttachtype(mf.getContentType());
      dbUsers.setUfAttachdata(mf.getBytes());
    }

    usersDao.updateUser(dbUsers);

    if (request.getTagIds() != null) {
      UserTagRequest tagRequest = new UserTagRequest();
      tagRequest.setUserId(request.getUserId());
      tagRequest.setTagIds(request.getTagIds());
      userTagService.update(tagRequest);
    }

    return usersDao.selectUserById(request.getUserId());
  }

  public int delete(int userId) {
    return usersDao.deleteUser(userId);
  }

  public List<Users> searchUsers(String keyword) {
    if (keyword == null || keyword.trim().isEmpty()) {
      return usersDao.selectAllUsers(); // 전체 조회용 쿼리 실행
    }
    return usersDao.searchUsers(keyword);
  }
}