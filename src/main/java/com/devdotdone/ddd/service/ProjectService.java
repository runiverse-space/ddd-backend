package com.devdotdone.ddd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.devdotdone.ddd.dao.ProjectDao;
import com.devdotdone.ddd.dto.project.Project;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectService {
  @Autowired
  private ProjectDao projectDao;

  public void create(Project project){
    projectDao.insertProject(project);
    log.info("insertProject 실행");
  }

  public Project getProject(int projectId){
    Project project = projectDao.selectProjectById(projectId);
    return project;
  }

  /* 
  public Project update(Project project){

    Project dbProject = projectDao.selectProjectById(project.getProjectId());
    if(dbProject==null){
      return null;
    }else{
      if(StringUtils.hasText(project.getProjectTitle())){
        dbProject.setProjectTitle(project.getProjectTitle());
      }
      if(StringUtils.hasText(project.getProjectContent())){
        dbProject.setProjectContent(project.getProjectContent());
      }
      
    }
  }
*/
  public int delete(int projectId){
    int rows = projectDao.deleteProject(projectId);
    return rows;
  }

  

}
