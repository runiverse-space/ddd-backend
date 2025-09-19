package com.devdotdone.ddd.dto.users;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Users {
  private int userId;
  private String userLoginId;
  private String userPassword;
  private String userName;
  private String userEmail;
  private String userIntro;
  private Date userCreateAt;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private MultipartFile ufAttach;

  private String ufAttachoname;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private String ufAttachsname;
  private String ufAttachtype;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private byte[] ufAttachdata;
}
