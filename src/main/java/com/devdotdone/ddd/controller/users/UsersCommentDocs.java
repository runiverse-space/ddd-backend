package com.devdotdone.ddd.controller.users;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.devdotdone.ddd.dto.users.LoginForm;
import com.devdotdone.ddd.dto.users.Users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "사용자", description = "사용자 관련 API")
public interface UsersCommentDocs {

  @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다")
  Map<String, Object> create(@Valid Users users) throws IOException;

  @Operation(summary = "로그인", description = "로그인 양식을 입력하여 로그인합니다")
  Map<String, Object> login(@RequestBody LoginForm loginForm);

  @Operation(summary = "사용자 번호로 조회", description = "사용자 번호로 사용자 정보를 조회합니다")
  Map<String, Object> detailId(@RequestParam("userId") int userId);

  @Operation(summary = "로그인 아이디로 조회", description = "로그인 아이디로 사용자 정보를 조회합니다")
  Map<String, Object> detailLoginId(@RequestParam("userLoginId") String userLoginId);

  @Operation(summary = "이메일로 조회", description = "이메일로 사용자 정보를 조회합니다")
  Map<String, Object> detailEmail(@RequestParam("userEmail") String userEmail);

  @Operation(summary = "첨부파일 다운로드", description = "사용자 정보에 있는 첨부파일을 다운로드합니다")
  void ufAttachDownload(@RequestParam("userId") int userId, HttpServletResponse response) throws Exception;

  @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다")
  Map<String, Object> update(Users users) throws Exception ;

  @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다(회원탈퇴)")
  Map<String, Object> delete(@RequestParam("userId") int userId);
}