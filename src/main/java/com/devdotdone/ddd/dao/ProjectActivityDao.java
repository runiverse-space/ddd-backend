package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.project.ProjectActivity;

@Mapper
public interface ProjectActivityDao {
  public int insertActivity(ProjectActivity projectActivity);
  public ProjectActivity selectById(int activityId);
  public List<ProjectActivity> selectByReceiverId(int userId);
  public int updateActivityStatus(ProjectActivity projectActivity);
  public int changeToRead(int activityId);
  public int delete(int activityId);
}
