package com.devdotdone.ddd.dto.schedule;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScheduleRequest {
  private int scheduleId;
  private int projectId;
  private int userId;
  private String scheduleTitle;
  private String scheduleContent;
  private Date scheduleStartDate;
  private Date scheduleEndDate;
  private String scheduleStatus;
  private List<Integer> userIds;

  // private Schedule schedule;
  // private List<Long> userIds;
}
