package com.devdotdone.ddd.controller.userprojectrole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;
import com.devdotdone.ddd.service.UserProjectRoleService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/userprojectrole")
public class UserProjectRoleController {

  @Autowired
  private UserProjectRoleService userProjectRoleService;

 

  // 프로젝트 멤버 목록 조회
  @GetMapping("/memberlist")
  public List<UserProjectRole> memberList(@RequestParam("projectId") int projectId) {

    List<UserProjectRole> projectmemberlist = userProjectRoleService.getProjectMember(projectId);
    log.info(projectmemberlist.toString());

    return projectmemberlist;

  }

  //특정 유저의 프로젝트 목록조회
  @GetMapping("/projectlist")
  public Map<String,Object> getUsersProjectRoles(@RequestParam("userId") int userId){
    Map<String,Object> result = new HashMap<>();
    
    List<UserProjectRole> roles = userProjectRoleService.getProjectsListByUserId(userId);
    result.put("result","success");
    result.put("data", roles);
    
    return result;

  }
  

  // 새로운 멤버 영입(총인원 6명인 조건 그대로)
  @PostMapping("/insertmembers")
  public Map<String, String> addMember(@RequestBody UserProjectRole userProjectRole) {
   
      userProjectRoleService.assignUsersToProject(
          userProjectRole.getProjectId(),
          userProjectRole.getUserId(),
          userProjectRole.getUprRole());
  

    String role = userProjectRoleService.getUserProjectRole(userProjectRole.getProjectId(),
        userProjectRole.getUserId());
    log.info(role.toString());
    Map<String, String> map = new HashMap<>();
    map.put("result", "success");
    map.put("role", role);
    return map;
  }


  // 팀장 권한 위임
  @PostMapping("/changeadmin")
  public Map<String,Object> changeAdmin(@RequestBody UserProjectRole userProjectRole){
    
    Map<String,Object> response = new HashMap<>();

    try {
      int result= userProjectRoleService.updateAdmin(userProjectRole);
      response.put("suceess",true);
      response.put("result",result);
    
    } catch (Exception e) {
      response.put("suceess",false);
      response.put("result",0);
    }
    
    return response;
      
     
  }
  
  // 프로젝트 멤버 수 조회
  @GetMapping("/count")
  public Map<String,Object> countProjectMembers(@RequestParam("projectId") int projectId){
    Map<String,Object> result= new HashMap<>()  ;
    int total =userProjectRoleService.getCountProjectMembers(projectId);
    log.info("프로젝트 인원 총 {}",total+" 명입니다.");
    result.put("success",true);
    result.put("total", total);
    
    return result;

  }

  // 프로젝터에서 멤버 빼기
  @DeleteMapping("/delete")
  public String deleteMembers(@RequestParam("projectId")int projectId,@RequestParam("userId") int userId){
    userProjectRoleService.deleteUsersFromProject(projectId,userId );

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("result", "success");

    return jsonObject.toString();

  }



}
