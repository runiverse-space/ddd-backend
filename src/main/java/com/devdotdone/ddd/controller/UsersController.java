package com.devdotdone.ddd.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devdotdone.ddd.dto.users.LoginForm;
import com.devdotdone.ddd.dto.users.Users;
import com.devdotdone.ddd.service.JwtService;
import com.devdotdone.ddd.service.UsersService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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

    Users users = usersService.getUsersByLoginId(loginForm.getUserLoginId());
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

  @GetMapping("/detail/id")
  public Map<String, Object> detailId(@RequestParam("userId") int userId) {
    Map<String, Object> resultMap = new HashMap<>();
    Users users = usersService.getUsers(userId);
    resultMap.put("result", "success");
    resultMap.put("data", users);
    return resultMap;
  }

  @GetMapping("/detail/loginId")
  public Map<String, Object> detailLoginId(@RequestParam("userLoginId") String userLoginId) {
    Map<String, Object> resultMap = new HashMap<>();
    Users users = usersService.getUsersByLoginId(userLoginId);
    resultMap.put("result", "success");
    resultMap.put("data", users);
    return resultMap;
  }

  @GetMapping("/detail/email")
  public Map<String, Object> detailEmail(@RequestParam("userEmail") String userEmail) {
    Map<String, Object> resultMap = new HashMap<>();
    Users users = usersService.getUsersByEmail(userEmail);
    resultMap.put("result", "success");
    resultMap.put("data", users);
    return resultMap;
  }

  @GetMapping("/attach-download")
  public void boardAttachDownload(@RequestParam("userId") int userId, HttpServletResponse response) throws Exception {
    Users users = usersService.getUsers(userId);

    String fileName = users.getUfAttachoname();

    // 응답 헤더에 Content-Type 추가
    response.setContentType(users.getUfAttachtype());

    // 본문 내용을 파일로 저장할 수 있도록 헤더 추가
    String encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    response.setHeader("Content-Disposition", "attachment; file=\"" + encodedFileName + "\"");

    // 응답 본문으로 데이터를 출력하는 스트림
    OutputStream os = response.getOutputStream();
    BufferedOutputStream bos = new BufferedOutputStream(os);

    // 이미지 캐싱을 하지 않도록 헤더 추가
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, proxy-revalidate");
    response.setHeader(HttpHeaders.PRAGMA, "no-cache");
    response.setHeader(HttpHeaders.EXPIRES, "0");


    // 응답 본문에 파일 데이터 출력
    bos.write(users.getUfAttachdata());
    bos.flush();
    bos.close();
  }

  @PutMapping("/update")
  public Map<String, Object> update(Users users) throws Exception {
    Map<String, Object> map = new HashMap<>();

    try {
      MultipartFile mf = users.getUfAttach();
      if (mf != null && !mf.isEmpty()) {
        users.setUfAttachoname(mf.getOriginalFilename());
        users.setUfAttachtype(mf.getContentType());
        users.setUfAttachdata(mf.getBytes());
      }

      Users dbUsers = usersService.update(users);
      if (dbUsers == null) {
        map.put("result", "fail");
        map.put("message", "해당 사용자가 없습니다.");
      } else {
        map.put("result", "success");
        map.put("users", dbUsers);
      }
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @DeleteMapping("/delete")
  public Map<String, Object> delete(@RequestParam("userId") int userId) {
    Map<String, Object> map = new HashMap<>();

    try {
      int rows = usersService.deleteUser(userId);
      if (rows == 0) {
        map.put("result", "fail");
        map.put("message", "삭제 실패");
      } else {
        map.put("result", "success");
        map.put("message", "사용자가 삭제되었습니다.");
      }
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }

}
