package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.knowledge.KnowledgeComment;

/* 
-- [지식 댓글]
CREATE TABLE knowledgeComment (
    knowledgeComment_id    NUMBER PRIMARY KEY,
    knowledge_id  NUMBER NOT NULL,
    user_id     NUMBER NOT NULL, 
    knowledgeCommentContent       CLOB NOT NULL,
    knowledgeCommentCreatedAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_kc_knowledge FOREIGN KEY (knowledge_id) REFERENCES knowledge(knowledge_id),
    CONSTRAINT fk_kc_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);
-- updated시간도  포함하도록 테이블 수정함
alter table KNOWLEDGECOMMENT
     add knowledgeCommentUpdatedAt TIMESTAMP
     DEFAULT CURRENT_TIMESTAMP;

 
 */


@Mapper
public interface KnowledgeCommentDao {
    //댓글 작성
    public int insertKnowledgeComment(KnowledgeComment knowledgeComment);
    //코멘트 수정
    public int updateKnowledgeCommentByKnowledgeCommentId(KnowledgeComment knowledgeComment);


   //특정 지식창고의 댓글 목록 조회
    public List<KnowledgeComment> selectKnowledgeCommentByKnowledgeId(int knowledgeId);
   //댓글 상세 조회
    public KnowledgeComment selectKnowledgeCommentByKnowledgeCommentId(int knowledgeCommentId);

  //public List<KnowledgeComment> selectByPage(Pager pager);

  
  //특정 지식글의 댓글 수 조회
  public int countKnowledgeCommentsByKnowledgeId(int knowledgeId);

  public int deleteKnowledgeComment(int knowledgeCommentId);

  public int countAllKnowledgeComment();


}
