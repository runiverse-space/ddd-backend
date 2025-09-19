package com.devdotdone.ddd.dto.schedule;

import java.util.Date;

import lombok.Data;

@Data
public class Schedule {
  private int scheduleId;
  private int projectId;
  private int userId;   // 일정 만든 사용자
  private String scheduleTitle;
  private String scheduleContent;
  private Date scheduleStartDate;
  private Date scheduleEndDate;
  private String scheduleStatus;
  private Date scheduleCreatedAt;
}
