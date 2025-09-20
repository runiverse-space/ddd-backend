package com.devdotdone.ddd.dto.knowledge;

import java.sql.Date;

import lombok.Data;

@Data
public class KnowledgeComment {
    private int knowledgeCommentId;
    private int knowledgeId;
    private int userId;
    private String knowledgeCommentContent;
    private Date knowledgeCommentCreatedAt;
}
