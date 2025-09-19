package com.devdotdone.ddd.dto.users;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

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

  private MultipartFile ufAttach;
  private String ufAttachoname;
  private String ufAttachsname;
  private String ufAttachtype;
  private byte[] ufAttachdata;
}
