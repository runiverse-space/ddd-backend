package com.devdotdone.ddd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  }

  public Project getProject(int projectId){
    Project project = projectDao.selectProjectById(projectId);
    return project;
  }

  

}
