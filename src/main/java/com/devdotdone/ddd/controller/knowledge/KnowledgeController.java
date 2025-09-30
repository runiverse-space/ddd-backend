package com.devdotdone.ddd.controller.knowledge;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devdotdone.ddd.dto.knowledge.Knowledge;
import com.devdotdone.ddd.service.KnowledgeService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
  @Autowired
  KnowledgeService knowledgeService;

  /*
   * 지식창고 글 생성
   */
  @PostMapping("/create")
  public Knowledge create(@ModelAttribute Knowledge knowledge) throws Exception {

    log.info(knowledge.toString());

    MultipartFile mf = knowledge.getKfAttach();

    if (mf != null && !mf.isEmpty()) { // mf가 null 아니고 안에 파일이 있다
      knowledge.setKfAttachoname(mf.getOriginalFilename());// 사용자가 올린 파일이름
      knowledge.setKfAttachtype(mf.getContentType());
      knowledge.setKfAttachdata(mf.getBytes());
    }

    knowledgeService.create(knowledge);
    Knowledge dbknowledge = knowledgeService.getKnowledge(knowledge.getKnowledgeId());

    return dbknowledge;
  }

  /*
   * 리스트
   */

  @GetMapping("/list")
  public List<Knowledge> knowledgeList(@RequestParam("projectId") int projectId) {

    List<Knowledge> list = knowledgeService.getKnowledgeListByProject(projectId);

    return list;

  }

  /*
   * 특정 지식창고 글 자세히보기
   */
  @GetMapping("/detail")
  public Map<String, Object> detail(@RequestParam("knowledgeId") int knowledgeId) {

    Map<String, Object> resultMap = new HashMap<>();
    Knowledge knowledge = knowledgeService.getKnowledge(knowledgeId);
    resultMap.put("result", "success");
    resultMap.put("data", knowledge);
    return resultMap;

  }

  /*
   * 지식 창고 수정하기
   */
  @PutMapping("/update")
  public Knowledge update(@RequestBody Knowledge knowledge) {
    log.info("지식창고 수정하기", knowledge.toString());

    knowledgeService.update(knowledge);// 반환값 무시하고

    Knowledge dbKnowledge = knowledgeService.getKnowledge(knowledge.getKnowledgeId());

    return dbKnowledge;

  }

  /*
   * 지식 창고 삭제하기
   */

  @DeleteMapping("/delete")
  public String delete(@RequestParam("knowledgeId") int knowledgeId) {
    knowledgeService.delete(knowledgeId);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("result", "success");

    return jsonObject.toString();
  }

  /*
   * 지식창고 파일 다운 받기
   */

  @GetMapping("/attachdownload")
  public void attachdownload(@RequestParam("knowledgeId") int knowledgeId, HttpServletResponse response)
      throws Exception {

    // db에서 내용을 읽음
    Knowledge knowledge = knowledgeService.getKnowledge(knowledgeId);

    String fileName = knowledge.getKfAttachoname();

    response.setContentType(knowledge.getKfAttachtype());

    String encodedfileName = new String(fileName.getBytes("UTF-8"), "ISO_8859-1");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");

    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, proxy-revalidate");
    response.setHeader(HttpHeaders.PRAGMA, "no-cache");
    response.setHeader(HttpHeaders.EXPIRES, "0");

    OutputStream os = response.getOutputStream();
    BufferedOutputStream bos = new BufferedOutputStream(os);

    bos.write(knowledge.getKfAttachdata());
    bos.flush();
    bos.close();

  }

}
