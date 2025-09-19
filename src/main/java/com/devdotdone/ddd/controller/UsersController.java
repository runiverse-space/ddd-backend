package com.devdotdone.ddd.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devdotdone.ddd.dto.users.Users;
import com.devdotdone.ddd.service.UsersService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {
  @Autowired
  private UsersService usersService;

  @PostMapping("/create")
  public Map<String, Object> create(Users users) throws IOException {
    log.info(users.toString());

    MultipartFile mf = users.getUfAttach();
    if (mf != null && !mf.isEmpty()) {
      users.setUfAttachoname(mf.getOriginalFilename());
      users.setUfAttachtype(mf.getContentType());
      users.setUfAttachdata(mf.getBytes());
    }

    Map<String, Object> map = new HashMap<>();
    try {
      usersService.insertUser(users);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }
  
}
