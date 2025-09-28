package com.devdotdone.ddd.dto.userProjectRole;

import lombok.Data;

@Data
public class UserProjectRole {
  private int uprId;
  private int userId;
  private int projectId;
  private String uprRole;
}