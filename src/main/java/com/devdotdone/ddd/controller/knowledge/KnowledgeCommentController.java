package com.devdotdone.ddd.controller.knowledge;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.knowledge.KnowledgeComment;
import com.devdotdone.ddd.dto.knowledge.KnowledgeCommentRequest;
import com.devdotdone.ddd.service.KnowledgeCommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/knowledgecomment")
public class KnowledgeCommentController implements KnowledgeCommentDocs{
  
  @Autowired
  private KnowledgeCommentService knowledgeCommentService;

  @PostMapping("/create")
  public KnowledgeComment create(@RequestBody KnowledgeCommentRequest  knowledgeCommentRequest){
    log.info("댓글 생성 요청:{}",knowledgeCommentRequest.toString());
    
    KnowledgeComment createdComment= knowledgeCommentService.create(knowledgeCommentRequest);
    return createdComment;
  
  }


  @GetMapping("/read")
  public Map<String,Object> read(@RequestParam("knowledgeId") int knowledgeId){
    Map<String,Object> resultMap = new HashMap<>();
    KnowledgeComment knowledgeComment= knowledgeCommentService.getKnowledgeComment(knowledgeId);
    resultMap.put("result","success");
    resultMap.put("data",knowledgeComment);

    return resultMap;


  }

  @PutMapping("/update")
  public KnowledgeComment update(@RequestBody KnowledgeCommentRequest knowledgeCommentRequest){
    log.info("댓글 수정 요청:{}",knowledgeCommentRequest.toString());

    KnowledgeComment dbknowledgeComment= knowledgeCommentService.update(knowledgeCommentRequest);
    log.info("댓글 수정결과",dbknowledgeComment.toString());
    
    return dbknowledgeComment;
  
  
  }

  @DeleteMapping("/delete")
  public String delete(@RequestParam("knowledgeCommentId")int knowledgeCommentId){
    knowledgeCommentService.delete(knowledgeCommentId);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("result", "success");
    return jsonObject.toString();


  }
     

  

  
}
