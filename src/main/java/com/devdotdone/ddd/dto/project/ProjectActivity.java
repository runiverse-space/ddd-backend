package com.devdotdone.ddd.dto.project;

import java.util.Date;

import lombok.Data;

@Data
public class ProjectActivity {
  private int activityId;
  private String paType;
  private int senderId;
  private int receiverid;
  private int projectId;
  private String paMessage;
  private String paStatus;
  private String paIsRead;
  private Date paCreatedAt;
  private Date paUpdatedAt;
}

