package com.devdotdone.ddd.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  @PostMapping("/create")
  public Project create(@RequestBody Project project) {
      log.info(project.toString());
      
      projectService.create(project);
      Project dbProject= projectService.getProject(project.getProjectId());
    

      return dbProject;
  }
/*
  @PostMapping("/update")
  public Map<String, Object> update(@RequestBody Project project){
    Project dbProject = projectService.update(project);

    Map<String,Object> map = new HashMap<>();

    if(dbProject==null){
      map.put("result","fail");
    }else{
      map.put("result","success");
      map.put("member",dbProject);
    }
    
    return map;
 
  }
 */
  @DeleteMapping("/delete")
  public String delete(@RequestParam("projectId") int projectId){
    projectService.delete(projectId);
    JSONObject jsonObject= new JSONObject();
    jsonObject.put("result","success");

    return jsonObject.toString();//{"result":"success"}
  }



  
}
