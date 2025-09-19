package com.devdotdone.ddd.dao;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectMilestone;

/*
CREATE TABLE projectMilestone (
    milestone_id      NUMBER PRIMARY KEY, -- 단계 ID
    project_id    NUMBER NOT NULL,    -- 어떤 프로젝트인지
    milestoneTitle         VARCHAR2(100) NOT NULL, -- 단계 이름 (예: 기획, 개발, 테스트)
    milestoneDate    DATE NOT NULL,
    CONSTRAINT fk_milestone_project FOREIGN KEY (project_id) REFERENCES project(project_id)
);
 */




@Mapper
public interface ProjectMilestoneDao {

  public int insertMilestone(ProjectMilestone projectMilestone );
  public ProjectMilestone selectMilestonesByProject(int projectMilestoneId);
  public int updateMilestone();
  public int deleteMilestone();
  
 

}
