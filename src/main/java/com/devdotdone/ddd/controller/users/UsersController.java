package com.devdotdone.ddd.controller.users;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devdotdone.ddd.dto.users.LoginForm;
import com.devdotdone.ddd.dto.users.Users;
import com.devdotdone.ddd.dto.users.UsersLoginRequest;
import com.devdotdone.ddd.dto.users.UsersResponse;
import com.devdotdone.ddd.dto.users.UsersSignupRequest;
import com.devdotdone.ddd.dto.users.UsersUpdateRequest;
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
  public Map<String, Object> create(@RequestBody @Valid UsersSignupRequest request) {
    Map<String, Object> map = new HashMap<>();
    try {
      usersService.create(request);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @PostMapping("/login")
  public Map<String, Object> login(@RequestBody UsersLoginRequest request) {
    Map<String, Object> map = new HashMap<>();
    try {
      // 로그인 아이디로 사용자 조회
      Users users = usersService.getUsersByLoginId(request.getUserLoginId());

      // 비밀번호 비교
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      if (!encoder.matches(request.getUserPassword(), users.getUserPassword())) {
        throw new IllegalArgumentException("비밀번호 불일치");
      }

      // JWT 토큰 발급
      String jwt = jwtService.createJwt(users.getUserId(), users.getUserLoginId(), users.getUserEmail());

      // 응답 데이터
      map.put("result", "success");
      map.put("userId", users.getUserId());
      map.put("userLoginId", users.getUserLoginId());
      map.put("jwt", jwt);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @PutMapping("/update")
  public Map<String, Object> update(@ModelAttribute UsersUpdateRequest request) {
    Map<String, Object> map = new HashMap<>();
    try {
      Users updated = usersService.updateProfile(request);
      map.put("result", "success");
      map.put("users", toResponse(updated));
      map.put("tags", request.getTagIds());
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
      int rows = usersService.delete(userId);
      if (rows == 0)
        throw new IllegalArgumentException("삭제 실패");
      map.put("result", "success");
      map.put("message", "사용자가 삭제되었습니다.");
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/detail/id")
  public Map<String, Object> detailById(@RequestParam("userId") int userId) {
    Map<String, Object> map = new HashMap<>();
    try {
      Users users = usersService.getUsers(userId);
      map.put("result", "success");
      map.put("data", toResponse(users));
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/detail/loginId")
  public Map<String, Object> detailByLoginId(@RequestParam("userLoginId") String userLoginId) {
    Map<String, Object> map = new HashMap<>();
    try {
      Users users = usersService.getUsersByLoginId(userLoginId);
      map.put("result", "success");
      map.put("data", toResponse(users));
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/detail/email")
  public Map<String, Object> detailByEmail(@RequestParam("userEmail") String userEmail) {
    Map<String, Object> map = new HashMap<>();
    try {
      Users users = usersService.getUsersByEmail(userEmail);
      map.put("result", "success");
      map.put("data", toResponse(users));
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/attach-download")
  public void ufAttachDownload(@RequestParam("userId") int userId,
      HttpServletResponse response) throws Exception {
    Users users = usersService.getUsers(userId);

    String fileName = users.getUfAttachoname();

    if (fileName != null) {
      // 응답 헤더에 Content-Type 추가
      response.setContentType(users.getUfAttachtype());

      // 본문 내용을 파일로 저장할 수 있도록 헤더 추가
      String encodedFileName = new String(fileName.getBytes("UTF-8"),
          "ISO-8859-1");
      response.setHeader("Content-Disposition", "attachment; file=\"" +
          encodedFileName + "\"");

      // 응답 본문으로 데이터를 출력하는 스트림
      OutputStream os = response.getOutputStream();
      BufferedOutputStream bos = new BufferedOutputStream(os);

      // 이미지 캐싱을 하지 않도록 헤더 추가
      response.setHeader(HttpHeaders.CACHE_CONTROL,
          "no-store, no-cache, must-revalidate, proxy-revalidate");
      response.setHeader(HttpHeaders.PRAGMA, "no-cache");
      response.setHeader(HttpHeaders.EXPIRES, "0");

      // 응답 본문에 파일 데이터 출력
      bos.write(users.getUfAttachdata());
      bos.flush();
      bos.close();
    }
  }

  @GetMapping("/search")
  public Map<String, Object> searchUsers(@RequestParam("keyword") String keyword) {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("result", "success");
      map.put("data", usersService.searchUsers(keyword));
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  private UsersResponse toResponse(Users users) {
    if (users == null)
      return null;
    UsersResponse r = new UsersResponse();
    r.setUserId(users.getUserId());
    r.setUserLoginId(users.getUserLoginId());
    r.setUserName(users.getUserName());
    r.setUserEmail(users.getUserEmail());
    r.setUserIntro(users.getUserIntro());
    r.setUfAttachoname(users.getUfAttachoname());
    r.setUfAttachtype(users.getUfAttachtype());
    return r;
  }

}