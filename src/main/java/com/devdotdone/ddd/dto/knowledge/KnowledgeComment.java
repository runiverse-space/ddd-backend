package com.devdotdone.ddd.dto.knowledge;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class KnowledgeComment {
    private int knowledgeCommentId;
    private int knowledgeId;
    private int userId;
    private String knowledgeCommentContent;
    private Date knowledgeCommentCreatedAt;
    //업데이트한 시간 추가함
    private Date knowledgeCommentUpdatedAt;

    //수정된 날짜 표기하는 메서드
    public Date getDisplayDate(){
        return knowledgeCommentUpdatedAt !=null ? knowledgeCommentUpdatedAt : knowledgeCommentCreatedAt;
    }

    public boolean isUpdated(){
        return knowledgeCommentUpdatedAt !=null;
    }
    

}
