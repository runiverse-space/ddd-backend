package com.devdotdone.ddd.dto.users;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Users {
  private int userId;

  @NotBlank(message = "아이디는 필수 입력값입니다.")
  private String userLoginId;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문자, 숫자, 특수문자를 사용하세요.")
  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private String userPassword;

  @NotBlank(message = "사용자 이름은 필수 입력값입니다.")  
  private String userName;

  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(message = "이메일 형식으로 입력해주십시오.")
  private String userEmail;

  private String userIntro;
  private Date userCreatedAt;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private MultipartFile ufAttach;

  private String ufAttachoname;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private String ufAttachsname;
  private String ufAttachtype;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private byte[] ufAttachdata;
}
