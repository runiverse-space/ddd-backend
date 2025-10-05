package com.devdotdone.ddd.dto.project;

import java.util.Date;

import lombok.Data;
/*
CREATE TABLE projectMilestone (
    milestone_id      NUMBER PRIMARY KEY, -- 단계 ID
    project_id    NUMBER NOT NULL,    -- 어떤 프로젝트인지
    milestoneTitle         VARCHAR2(100) NOT NULL, -- 단계 이름 (예: 기획, 개발, 테스트)
    milestoneDate    DATE NOT NULL,
    CONSTRAINT fk_milestone_project FOREIGN KEY (project_id) REFERENCES project(project_id)
); 
 */


@Data
public class ProjectMilestone {
  private int milestoneId;
  private int projectId;
  private String milestoneTitle;
  private Date milestoneDate ;
  //private int milestoneSortOrder;

}
