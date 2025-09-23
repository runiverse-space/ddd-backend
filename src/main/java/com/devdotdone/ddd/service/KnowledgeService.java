package com.devdotdone.ddd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.KnowledgeDao;
import com.devdotdone.ddd.dto.knowledge.Knowledge;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KnowledgeService {

  @Autowired
  private KnowledgeDao knowledgeDao;

  //지식창고 글 생성
  public void create(Knowledge knowledge){
    knowledgeDao.insertKnowledge(knowledge);
  }

  //첨부 파일 없는거
  public Knowledge getKnowledge(int knowledgeId){
    Knowledge knowledge = knowledgeDao.selectKnowledgeByKnowledgeId(knowledgeId);
    return knowledge;
  }

  

}
