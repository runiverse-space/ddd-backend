package com.devdotdone.ddd.dto.users;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UsersResponse {
  private int userId;
  private String userLoginId;

  private String userName;
  private String userEmail;

  private String userIntro;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private MultipartFile ufAttach;

  private String ufAttachoname;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private String ufAttachsname;
  private String ufAttachtype;

  @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
  private byte[] ufAttachdata;
}
