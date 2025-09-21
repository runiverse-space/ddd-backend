package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.tag.ProjectTag;

@Mapper
public interface ProjectTagDao {
  // 프로젝트에 태그 추가
  public int insertProjectTag(ProjectTag projectTag);
  // 프로젝트에 포함된 태그 조회
  public List<ProjectTag> findTags(int projectid);
  // 프로젝트의 태그 제거
  public int deleteTag(int projectid);
}
