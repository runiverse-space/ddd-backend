package com.devdotdone.ddd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  @PostMapping("/project-create")
  public Project projectCreate(@RequestBody Project project) {
      log.info(project.toString());
      
      projectService.create(project);
      Project dbProject= projectService.getProject(project.getProjectId());


      return dbProject;
  }
  
}
