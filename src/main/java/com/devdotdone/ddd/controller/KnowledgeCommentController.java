package com.devdotdone.ddd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.knowledge.KnowledgeComment;
import com.devdotdone.ddd.dto.knowledge.KnowledgeCommentRequest;
import com.devdotdone.ddd.service.KnowledgeCommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/knowledgecomment")
public class KnowledgeCommentController {
  
  @Autowired
  private KnowledgeCommentService knowledgeCommentService;

  @PostMapping("/create")
  public KnowledgeComment create(@RequestBody KnowledgeCommentRequest  knowledgeCommentRequest){
    log.info("댓글 생성 요청:{}",knowledgeCommentRequest.toString());
    
    KnowledgeComment createdComment= knowledgeCommentService.create(knowledgeCommentRequest);
   
    
    return createdComment;
  
  }

  
}
