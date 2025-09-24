package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.knowledge.Knowledge;
/*
 
-- ================================
-- [지식 공유 (Knowledge)+file]
-- ================================
CREATE TABLE knowledge (
    knowledge_id  NUMBER PRIMARY KEY,
    project_id    NUMBER NOT NULL,
    user_id     NUMBER NOT NULL,
    knowledgeTitle         VARCHAR2(100) NOT NULL,
    knowledgeContent       CLOB NOT NULL,
    knowledgeUrl           VARCHAR2(255), -- 참고 링크
    
    kfattachoname    varchar(100)  null, --파일
    kfattachsname    varchar(100)  null,
    kfattachtype     varchar(100)  null,
    kfattachdata     blob          NULL,
    knowledgeCreatedAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kn_project FOREIGN KEY (project_id) REFERENCES project(project_id),
    CONSTRAINT fk_kn_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);
  

 */

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
