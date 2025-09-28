package com.devdotdone.ddd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.KnowledgeCommentDao;
import com.devdotdone.ddd.dao.KnowledgeDao;
import com.devdotdone.ddd.dto.knowledge.Knowledge;
import com.devdotdone.ddd.dto.knowledge.KnowledgeComment;
import com.devdotdone.ddd.dto.knowledge.KnowledgeCommentRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KnowledgeCommentService {
  
  @Autowired
  private KnowledgeCommentDao knowledgeCommentDao;

  @Autowired
  private KnowledgeDao knowledgeDao;

  //댓글 생성
  @Transactional
  public KnowledgeComment create(KnowledgeCommentRequest knowledgeCommentRequest){
      
      //knowledgeId 유효성 검사
      Knowledge existKnowledge = 
                knowledgeDao.selectKnowledgeByKnowledgeId(knowledgeCommentRequest.getKnowledgeId());
      
      if(existKnowledge==null){
        throw new IllegalArgumentException("존재하지 않는 지식글입니다.");
      }
    
    
      KnowledgeComment knowledgeComment= new KnowledgeComment();

      knowledgeComment.setKnowledgeId(knowledgeCommentRequest.getKnowledgeId());
      knowledgeComment.setUserId(knowledgeCommentRequest.getUserId());
      knowledgeComment.setKnowledgeCommentContent(knowledgeCommentRequest.getKnowledgeCommentContent());

      log.info("댓글 생성 요청: {}",knowledgeComment.toString());
      knowledgeCommentDao.insertKnowledgeComment(knowledgeComment);

      log.info("댓글 생성 완료- knowledgecommentId: {},knowledgeId: {}",
            knowledgeComment.getKnowledgeCommentId(),
            knowledgeComment.getKnowledgeId());
   
            return knowledgeComment;
    
  }

  //댓글 읽기
  public KnowledgeComment getKnowledgeComment(int knowledgeId){
    KnowledgeComment knowledgeComment= knowledgeCommentDao.selectKnowledgeCommentByKnowledgeCommentId(knowledgeId);
    return knowledgeComment;
  }
  
  //댓글 업데이트하기
  public KnowledgeComment update(KnowledgeCommentRequest knowledgeCommentRequest){

    KnowledgeComment knowledgeComment = knowledgeCommentDao.selectKnowledgeCommentByKnowledgeCommentId(knowledgeCommentRequest.getKnowledgeCommentId());

    if(knowledgeComment==null){
      return null;
    }

    log.info("수정전:{}",knowledgeComment);

    knowledgeComment.setKnowledgeCommentContent(knowledgeCommentRequest.getKnowledgeCommentContent());
    knowledgeComment.setKnowledgeCommentUpdatedAt(knowledgeCommentRequest.getKnowledgeCommentUpdatedAt());
    
    log.info(knowledgeComment.toString());
    knowledgeCommentDao.updateKnowledgeCommentByKnowledgeCommentId(knowledgeComment);
    
    return knowledgeComment;
  
  }

  //댓글 삭제하기
  public int delete(int knowledgeCommentId){
    int rows = knowledgeCommentDao.deleteKnowledgeComment(knowledgeCommentId);
    return rows;
  }





}
