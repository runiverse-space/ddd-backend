package com.devdotdone.ddd.dto.project;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ProjectActivity {
  private int activityId;
  private String paType;
  private int senderId;
  private int receiverId;
  private int projectId;
  private String paMessage;
  private String paStatus;
  private String paIsRead;
  private Timestamp paCreatedAt;
  private Timestamp paUpdatedAt;

  // 추가 필드(화면 표시용)
  private String senderName;
  private String projectTitle;
}

