package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.ProjectDao;
import com.devdotdone.ddd.dao.ProjectMilestoneDao;
import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectMilestone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectMilestoneService {
  
  private final ProjectMilestoneDao projectMilestoneDao;
  private final ProjectDao projectDao;

  // 프로젝트 마일스톤 생성
  public ProjectMilestone createMilestone(ProjectMilestone milestone) {
    // 프로젝트 존재 여부 확인
    Project project = projectDao.selectProjectById(milestone.getProjectId());
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }

    // 마일스톤 제목 검증
    if (milestone.getMilestoneTitle() == null || milestone.getMilestoneTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("마일스톤 제목은 필수입니다.");
    }

    // 마일스톤 날짜 검증 - 입력 여부
    if (milestone.getMilestoneDate() == null) {
      throw new IllegalArgumentException("마일스톤 날짜는 필수입니다.");
    }

    int result = projectMilestoneDao.insertMilestone(milestone);
    if (result <= 0) {
      throw new RuntimeException("마일스톤 생성에 실패했습니다.");
    }

    return milestone;
  }

  // 프로젝트의 모든 마일스톤 조회
  public List<ProjectMilestone> getMilestonesByProject(int projectId) {
    // 프로젝트 존재 여부 확인
    Project project = projectDao.selectProjectById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }

    return projectMilestoneDao.selectMilestonesByProject(projectId);
  }

  // 마일스톤 단건 조회
  public ProjectMilestone getMilestoneById(int milestoneId) {
    ProjectMilestone milestone = projectMilestoneDao.selectByProject(milestoneId);
    if (milestone == null) {
      throw new IllegalArgumentException("존재하지 않는 마일스톤입니다.");
    }

    return milestone;
  }

  // 마일스톤 수정
  @Transactional
  public ProjectMilestone updateMilestone(ProjectMilestone milestone) {
    // 기존 마일스톤 존재 여부 확인
    ProjectMilestone existing = projectMilestoneDao.selectByProject(milestone.getMilestoneId());
    if (existing == null) {
      throw new IllegalArgumentException("존재하지 않는 마일스톤입니다.");
    }

    // 업데이트할 필드 검증
    if (milestone.getMilestoneTitle() != null && milestone.getMilestoneTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("마일스톤 제목은 빈 값일 수 없습니다.");
    }

    int result = projectMilestoneDao.updateMilestone(milestone);
    if (result <= 0) {
      throw new RuntimeException("마일스톤 수정에 실패했습니다.");
    }

    return projectMilestoneDao.selectByProject(milestone.getMilestoneId());
  }

  // 마일스톤 삭제
  public int deleteMilestone(int milestoneId) {
    ProjectMilestone existing = projectMilestoneDao.selectByProject(milestoneId);
    if (existing == null) {
      throw new IllegalArgumentException("존재하지 않는 마일스톤입니다.");
    }

    int result = projectMilestoneDao.deleteMilestone(milestoneId);
    if (result <= 0) {
      throw new RuntimeException("마일스톤 삭제에 실패했습니다.");
    }

    return result;
  }

  // 프로젝트의 모든 마일스톤 삭제 (프로젝트 삭제시 사용)
  @Transactional
  public void deleteAllMilestonesByProject(int projectId) {
    // 프로젝트 존재 여부 확인
    Project project = projectDao.selectProjectById(projectId);    
    if (project == null) {
      throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
    }

    projectMilestoneDao.deleteAll(projectId);
    log.info("프로젝트 {}의 모든 마일스톤이 삭제되었습니다.", projectId);
  }
}
