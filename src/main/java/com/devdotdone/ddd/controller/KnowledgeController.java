package com.devdotdone.ddd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devdotdone.ddd.dao.KnowledgeDao;
import com.devdotdone.ddd.dto.knowledge.Knowledge;
import com.devdotdone.ddd.service.KnowledgeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

  @Autowired
  private KnowledgeDao knowledgeDao;

    KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

  @PostMapping("/create")
  public Knowledge create(@ModelAttribute Knowledge knowledge) throws Exception{
     
    log.info(knowledge.toString());
    
    MultipartFile mf =knowledge.getKfattach();
     
    if (mf != null && !mf.isEmpty()) { // mf가 null 아니고 안에 파일이 있다
      knowledge.setKfattachoname(mf.getOriginalFilename());// 사용자가 올린 파일이름
      knowledge.setKfattachtype(mf.getContentType());
      knowledge.setKfattachdata(mf.getBytes());
    }

    knowledgeService.create(knowledge);
    Knowledge dbknowledge= knowledgeService.getKnowledge(knowledge.getKnowledgeId());
    
    return dbknowledge;
  }
    
  
}
