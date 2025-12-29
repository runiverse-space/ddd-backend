package com.devdotdone.ddd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.ProjectActivityDao;
import com.devdotdone.ddd.dao.ProjectDao;
import com.devdotdone.ddd.dao.ProjectTagDao;
import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dao.UsersDao;
import com.devdotdone.ddd.dto.knowledge.Knowledge;
import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectMilestone;
import com.devdotdone.ddd.dto.project.ProjectRequest;
import com.devdotdone.ddd.dto.project.ProjectResponse;
import com.devdotdone.ddd.dto.schedule.Schedule;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;
import com.devdotdone.ddd.dto.users.Users;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectService {

  private final ProjectTagService projectTagService;
  @Autowired
  private ProjectDao projectDao;

  @Autowired
  private UsersDao usersDao;

  @Autowired
  private UserProjectRoleDao userProjectRoleDao;

  @Autowired
  private ProjectTagDao projectTagDao;

  @Autowired
  private UserProjectRoleService userProjectRoleService;

  @Autowired
  private ProjectMilestoneService projectMilestoneService;

  @Autowired
  private ProjectActivityDao projectActivityDao;

  @Autowired
  private ScheduleService scheduleService;

  @Autowired
  private KnowledgeService knowledgeService;

  private static final int MAX_MEMBERS = 6;

  ProjectService(ProjectTagService projectTagService) {
    this.projectTagService = projectTagService;
  }

  // 새 프로젝트 만들기
  @Transactional
  public Project create(ProjectRequest request) {
    // 생성자 존재 여부 확인
    // userdao없이 request.getUserId가 가능한건지 확인 (주말 확인)
    Users creator = usersDao.selectUserById(request.getUserId());
    if (creator == null) {
      throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    }

    // 프로젝트 생성
    Project project = new Project();
    project.setUserId(request.getUserId());
    project.setProjectTitle(request.getProjectTitle());
    project.setProjectContent(request.getProjectContent());
    project.setProjectStartDate(request.getProjectStartDate());
    project.setProjectEndDate(request.getProjectEndDate());

    int result = projectDao.insertProject(project);
    // System.out.println("result:" + result);
    log.info(project.toString());
    if (result <= 0) {
      throw new RuntimeException("프로젝트 생성에 실패했습니다.");
    }

    // 생성자를 ADMIN으로 할당 (다른 서비스에 위임)
    userProjectRoleService.assignUsersProjectAdmin(project.getProjectId(), request.getUserId());

    // 사용자 MEMBER로 추가
    for (int userId : request.getUserIds()) {
      userProjectRoleService.assignUsersToProject(project.getProjectId(), userId, "MEMBER");
    }

    // 초기 마일스톤 추가
    // for (ProjectMilestone milestone : request.getProjectMilestones()) {
    // milestone.setProjectId(project.getProjectId());
    // projectMilestoneService.createMilestone(milestone);
    // }

    return projectDao.selectProjectById(project.getProjectId());
  }

  // 프로젝트 단건 조회
  public Project getProjectById(int projectId) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }
    return project;
  }

  // 프로젝트 전부 조회
  public List<Project> getAllProjects() {
    return projectDao.selectAllProjects();
  }

  // 프로젝트 수정
  @Transactional
public ProjectResponse update(ProjectRequest request) {
  // 1. 프로젝트 존재 여부 확인
  Project project = projectDao.selectProjectById(request.getProjectId());
  if (project == null) {
    throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
  }

  // 2. 요청 데이터 검증
  if (request.getProjectTitle() == null || request.getProjectTitle().trim().isEmpty()) {
    throw new IllegalArgumentException("프로젝트 제목은 빈 값일 수 없습니다.");
  }

  // 3. 프로젝트 기본 정보 업데이트
  project.setProjectTitle(request.getProjectTitle());
  project.setProjectContent(request.getProjectContent());
  project.setProjectStartDate(request.getProjectStartDate());
  project.setProjectEndDate(request.getProjectEndDate());

  int result = projectDao.updateProject(project);
  if (result <= 0) {
    throw new RuntimeException("프로젝트 수정에 실패했습니다.");
  }

  int projectId = request.getProjectId();

  // 4. 멤버 삭제 (추가보다 먼저 처리)
  if (request.getRemoveUserIdList() != null && !request.getRemoveUserIdList().isEmpty()) {
    for (int userId : request.getRemoveUserIdList()) {
      userProjectRoleService.deleteUsersFromProject(projectId, userId);
    }
  }

  // 5. 멤버 추가
  if (request.getAddUserIdList() != null && !request.getAddUserIdList().isEmpty()) {
    for (int userId : request.getAddUserIdList()) {
      userProjectRoleService.assignUsersToProject(projectId, userId, "MEMBER");
    }
  }

  // 6. 마일스톤 수정
  if (request.getProjectMilestones() != null && !request.getProjectMilestones().isEmpty()) {
    for (ProjectMilestone milestone : request.getProjectMilestones()) {
      projectMilestoneService.updateMilestone(milestone);
    }
  }

  // 7. 최종 결과 반환
  Project updatedProject = projectDao.selectProjectById(projectId);
  List<UserProjectRole> members = userProjectRoleDao.selectProjectMembers(projectId);
  
  List<Integer> memberIds = new ArrayList<>();
  for (UserProjectRole member : members) {
    memberIds.add(member.getUserId());
  }

  ProjectResponse response = new ProjectResponse();
  response.setProjectId(updatedProject.getProjectId());
  response.setUserId(updatedProject.getUserId());
  response.setProjectTitle(updatedProject.getProjectTitle());
  response.setProjectContent(updatedProject.getProjectContent());
  
  // Date를 LocalDate/LocalDateTime으로 변환
  if (updatedProject.getProjectStartDate() != null) {
    response.setProjectStartDate(new java.sql.Date(updatedProject.getProjectStartDate().getTime()).toLocalDate());
  }
  if (updatedProject.getProjectEndDate() != null) {
    response.setProjectEndDate(new java.sql.Date(updatedProject.getProjectEndDate().getTime()).toLocalDate());
  }
  if (updatedProject.getProjectCreatedAt() != null) {
    response.setProjectCreatedAt(new java.sql.Timestamp(updatedProject.getProjectCreatedAt().getTime()).toLocalDateTime());
  }
  
  response.setMemberIds(memberIds);
  
  return response;
}

  // 프로젝트 삭제
  @Transactional
  public int remove(int projectId) {
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }

    // 프로젝트의 모든 마일스톤 삭제
    projectMilestoneService.deleteAllMilestonesByProject(projectId);
    // 프로젝트의 모든 멤버 역할 삭제
    userProjectRoleDao.deleteAllUserProjectRole(projectId);
    // 프로젝트의 모든 태그 삭제
    List<Tag> tags = projectTagDao.selectTagByProjectId(projectId);
    for (Tag tag : tags) {
      projectTagDao.deleteProjectTag(projectId, tag.getTagId());
    }
    // 프로젝트의 모든 일정 삭제
    List<Schedule> schedules = scheduleService.getListByProject(projectId);
    for (Schedule schedule : schedules) {
      scheduleService.remove(schedule.getScheduleId());
    }
    // 프로젝트의 모든 지식 삭제
    List<Knowledge> knowledges = knowledgeService.getKnowledgeListByProject(projectId);
    for (Knowledge knowledge : knowledges) {
      knowledgeService.delete(knowledge.getKnowledgeId());
    }
    // 프로젝트와 관련된 모든 알림 삭제
    projectActivityDao.deleteAllUserProjectRole(projectId);

    int result = projectDao.deleteProject(projectId);
    if (result <= 0) {
      throw new RuntimeException("프로젝트 삭제에 실패했습니다.");
    }

    log.info("프로젝트 {}가 삭제되었습니다.", projectId);
    return result;
  }

  public List<Project> getProjectsByUserId(int userId) {
    List<UserProjectRole> userProjects = userProjectRoleService.getProjectsListByUserId(userId);

    List<Project> projects = new ArrayList<>();
    for (UserProjectRole upr : userProjects) {
      Project project = projectDao.selectProjectById(upr.getProjectId());
      if (project != null) {
        projects.add(project);
      }
    }
    return projects;
  }

}
