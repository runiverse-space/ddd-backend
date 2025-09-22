package com.devdotdone.ddd.dto.users;

import lombok.Data;

@Data
public class LoginForm {
  private String userLoginId;
  private String userPassword;
}
