package com.devdotdone.ddd.dto.project;

import java.util.Date;

import lombok.Data;

@Data
public class Project {
  
  private int projectId;
  private int userId; // 만든 사람

  private String projectTitle;
  private String projectContent;

  private Date projectStartDate;
  private Date projectEndDate;
  private Date projectCreatedAt;

}

