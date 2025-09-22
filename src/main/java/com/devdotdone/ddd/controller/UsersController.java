package com.devdotdone.ddd.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devdotdone.ddd.dto.users.LoginForm;
import com.devdotdone.ddd.dto.users.Users;
import com.devdotdone.ddd.service.JwtService;
import com.devdotdone.ddd.service.UsersService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/users")
@Slf4j
public class UsersController {
  @Autowired
  private UsersService usersService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/create")
  public Map<String, Object> create(@Valid Users users) throws IOException {
    log.info(users.toString());

    MultipartFile mf = users.getUfAttach();
    if (mf != null && !mf.isEmpty()) {
      users.setUfAttachoname(mf.getOriginalFilename());
      users.setUfAttachtype(mf.getContentType());
      users.setUfAttachdata(mf.getBytes());
    }

    Map<String, Object> map = new HashMap<>();
    try {
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      String encodedPassword = passwordEncoder.encode(users.getUserPassword());
      users.setUserPassword(encodedPassword);
      usersService.insertUser(users);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @PostMapping("/login")
  public Map<String, Object> login(@RequestBody LoginForm loginForm) {
    Map<String, Object> map = new HashMap<>();
    
    Users users = usersService.getUser(loginForm.getUserLoginId());
    if (users == null) {
      map.put("result", "fail");
      map.put("message", "아이디가 없음");
    } else {
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      if (passwordEncoder.matches(loginForm.getUserPassword(), users.getUserPassword())) {
        String jwt = jwtService.createJwt(users.getUserLoginId(), users.getUserEmail());
        map.put("result", "success");
        map.put("userId", users.getUserLoginId());
        map.put("jwt", jwt);
      } else {
        map.put("result", "fail");
        map.put("message", "암호가 틀림");
      }
    }
    return map;
  }
  
  @GetMapping("/detail")
  public Map<String,Object> detail(@RequestParam("userId") int userId){
    Map<String, Object> resultMap= new HashMap<>();
    Users users= usersService.getUsers(userId);
    resultMap.put("result", "success");
    resultMap.put("data",users);
    return resultMap;
  }



  @PutMapping("/update")
  public Map<String, Object> update(Users users) throws Exception{
    log.info(users.toString());
    
    MultipartFile mf= users.getUfAttach();
    if(mf!=null && !mf.isEmpty()){
      users.setUfAttachoname(mf.getOriginalFilename());
      users.setUfAttachtype(mf.getContentType());
      users.setUfAttachdata(mf.getBytes());
    }
    log.info("----------파일 업데이트 시작");
    Users dbUsers= usersService.update(users);
log.info("----------파일 업데이트 실행 완료");
    Map<String, Object> map = new HashMap<>();
    if(map==null){
      map.put("result","fail");
    }else{
      map.put("result","success");
      map.put("users","dbUsers");
    }
    return map;

  }

  @DeleteMapping("/delete")
  public String delete(@RequestParam("userId")int userId){
    usersService.deleteUser(userId);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("result","success");

    return jsonObject.toString();

  }



  
}
