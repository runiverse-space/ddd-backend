package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.knowledge.Knowledge;

@Mapper
public interface KnowledgeDao {
  //생성
  public int insertKnowledge(Knowledge knowledge);
  //knowledgeId별 조회
  public Knowledge selectKnowledgeByKnowledgeId(int knowledgeId);
  //유저별 조회
  public Knowledge selectKnowledgeByUserId(int userId);
  //프로젝트별 조회
  public Knowledge selectKnowledgeByProjectId(int projectId);

  //수정
  public int update(Knowledge knowledge);

  //삭제
  public int delete(int knowledgeId);

  public int countAll();


 // public List<Knowledge> selectByPage(Pager pager);



}
