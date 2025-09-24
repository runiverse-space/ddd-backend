package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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
  public int insertMilestone(ProjectMilestone projectMilestone);
  public ProjectMilestone selectByProject(int milestoneId);
  public List<ProjectMilestone> selectMilestonesByProject(int projectId);
  public int updateMilestone(ProjectMilestone projectMilestone);
  public int deleteMilestone(int milestoneId);
  public int deleteAll(int projectId);
}
