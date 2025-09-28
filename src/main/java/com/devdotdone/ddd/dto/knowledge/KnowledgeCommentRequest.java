package com.devdotdone.ddd.dto.knowledge;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class KnowledgeCommentRequest {
    private int knowledgeCommentId;
    private int knowledgeId;
    private String knowledgeCommentContent;
    private int userId;
    private Timestamp knowledgeCommentCreatedAt;
    private Timestamp knowledgeCommentUpdatedAt;
    

}
