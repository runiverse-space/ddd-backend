package com.devdotdone.ddd.controller.knowledge;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.devdotdone.ddd.dto.knowledge.KnowledgeComment;
import com.devdotdone.ddd.dto.knowledge.KnowledgeCommentRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="지식창고 댓글", description = "지식창고 댓글 관련 API")
public interface KnowledgeCommentDocs {

  @Operation(summary = "지식창고 댓글 생성", description = "새로운 지식창고를 생성합니다")
 KnowledgeComment create(@RequestBody KnowledgeCommentRequest  knowledgeCommentRequest);

  @Operation(summary = "지식창고 댓글 조회", description = "댓글의 아이디로 지식창고 댓글를 조회합니다")
  Map<String,Object> read(@RequestParam("knowledgeId") int knowledgeId);

   @Operation(summary = "지식창고 댓글 수정", description = "지식창고를 댓글 수정합니다")
   KnowledgeComment update(@RequestBody KnowledgeCommentRequest knowledgeCommentRequest);
       

  @Operation(summary = "지식창고 댓글 삭제", description = "지식창고를 댓글 삭제합니다")
  String delete(@RequestParam("knowledgeCommentId")int knowledgeCommentId);
} 