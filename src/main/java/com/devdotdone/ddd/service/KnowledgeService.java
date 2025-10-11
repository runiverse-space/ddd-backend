package com.devdotdone.ddd.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.KnowledgeDao;
import com.devdotdone.ddd.dao.KnowledgeTagDao;
import com.devdotdone.ddd.dto.knowledge.Knowledge;
import com.devdotdone.ddd.dto.tag.Tag;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KnowledgeService {

  @Autowired
  private KnowledgeDao knowledgeDao;

  @Autowired
  private KnowledgeTagDao knowledgeTagDao;

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
  //업데이트
  public int update(Knowledge knowledge){
    int rows =knowledgeDao.updateKnowledge(knowledge);
    return rows;
  }
  //삭제
  public int delete(int knowledgeId){
    
    List<Tag> tags= knowledgeTagDao.selectTagByKnowledgeId(knowledgeId);
    
    for(Tag tag: tags){
      knowledgeTagDao.deleteKnowledgeTag(knowledgeId, tag.getTagId());
    }
   
   
    int rows= knowledgeDao.deleteKnowledge(knowledgeId);
   

    return rows;
  }

  //이전글 조회
  public Knowledge getPrevKnowledge(int knowledgeId,int projectId){
    return knowledgeDao.selectPrevKnowledge(knowledgeId,projectId);
  }

  //다음글 조회
  public Knowledge getNextKnowledge(int knowledgeId,int projectId){
    return knowledgeDao.selectNextKnowledge(knowledgeId,projectId);
  }


}
