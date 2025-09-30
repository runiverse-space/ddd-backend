package com.devdotdone.ddd.service;

import java.util.List;

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

  //첨부 파일 없는거 1개 조회
  public Knowledge getKnowledge(int knowledgeId){
    Knowledge knowledge = knowledgeDao.selectKnowledgeByKnowledgeId(knowledgeId);
    return knowledge;
  }

  //특정 project의 지식창고 목록 조회
  public List<Knowledge> getKnowledgeListByProject(int projectId){
    return knowledgeDao.selectKnowledgeByProjectId(projectId);
  }

  //특정 user의 지식창고 목록 조회
  public List<Knowledge> getKnowledgesListByUser(int userId){
    return knowledgeDao.selectKnowledgeByUserId(userId);
  }

  public int update(Knowledge knowledge){
    int rows =knowledgeDao.updateKnowledge(knowledge);
    return rows;
  }

  public int delete(int knowledgeId){
    int rows= knowledgeDao.deleteKnowledge(knowledgeId);
    return rows;
  }

}
