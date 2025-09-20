package com.devdotdone.ddd.dto.users;

import lombok.Data;

@Data
public class UserProjectRole {
  private int uprId;
  private int userId;
  private int projectId;
  private String uprRole;

}