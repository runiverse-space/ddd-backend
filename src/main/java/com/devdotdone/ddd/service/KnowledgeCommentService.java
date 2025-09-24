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

  public KnowledgeComment getKnowledgeComment(int knowledgeCommentId){
    KnowledgeComment knowledgeComment= knowledgeCommentDao.selectKnowledgeCommentByKnowledgeCommentId(knowledgeCommentId);
    return knowledgeComment;
  }






}
