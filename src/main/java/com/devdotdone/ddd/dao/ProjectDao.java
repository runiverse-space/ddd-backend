package com.devdotdone.ddd.dao;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.project.Project;


/*
CREATE TABLE project (
    project_id    NUMBER PRIMARY KEY, -- 프로젝트 ID (고유 ID)
    user_id       NUMBER NOT NULL,     
    projectTitle   VARCHAR2(200) NOT NULL, -- 프로젝트 이름
    projectContent  CLOB,            -- 프로젝트 설명
    projectStartDate    DATE,                   -- 시작일
    projectEndDate      DATE,                   -- 종료일
    projectCreatedAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);
 */

@Mapper
public interface ProjectDao{

  public int insertProject(Project project);

  public Project selectProjectById(int projectId);

  
  public int updateProject(Project project);
  
  public int deleteProject(int projectId);
  
  public Project countProjectsByUserId(int projectId);
  
  public int countAllProjects();

  public int insertMilestone();

}
