package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.tag.UserTag;

@Mapper
public interface UserTagDao {
  // 프로젝트에 태그 추가
  public int insertUserTag(UserTag projectTag);
  // 프로젝트에 포함된 태그 조회
  public List<UserTag> findTags(int userId);
  // 프로젝트의 태그 제거
  public int deleteTag(int userId);
}
