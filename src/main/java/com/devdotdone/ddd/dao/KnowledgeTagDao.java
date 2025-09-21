package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.tag.KnowledgeTag;

@Mapper
public interface KnowledgeTagDao {
  // 지식에 태그 추가
  public int insertKnowledgeTag(KnowledgeTag knowledgeTag);
  // 지식에 포함된 태그 조회
  public List<KnowledgeTag> findTags(int knowledgeId);
  // 지식의 태그 제거
  public int deleteTag(int knowledgeId);
}
