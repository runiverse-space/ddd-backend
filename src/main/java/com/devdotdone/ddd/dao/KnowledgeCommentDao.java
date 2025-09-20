package com.devdotdone.ddd.dao;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.knowledge.KnowledgeComment;

@Mapper
public interface KnowledgeCommentDao {
   public int insert(KnowledgeComment knowledgeComment);

  public KnowledgeComment selectKnowledgeCommentByKnowledgeCommentId(int knowledgeCommentId);

  //public List<knowledgeCommentId> selectByPage(Pager pager);

  public int update(KnowledgeComment knowledgeComment);

  public int delete(int knowledgeCommentId);

  public int countAllKnowledgeComment();
}
