package com.devdotdone.ddd.dto.knowledge;


import java.sql.Timestamp;

import lombok.Data;

/*
 -- [지식 댓글]
CREATE TABLE knowledgeComment (
    knowledgeComment_id    NUMBER PRIMARY KEY,
    knowledge_id  NUMBER NOT NULL,
    user_id     NUMBER NOT NULL, 
    knowledgeCommentContent       CLOB NOT NULL,
    knowledgeCommentCreatedAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kc_knowledge FOREIGN KEY (knowledge_id) REFERENCES knowledge(knowledge_id),
    CONSTRAINT fk_kc_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);
 */



@Data
public class KnowledgeComment {
    private int knowledgeCommentId;
    private int knowledgeId;
    private int userId;
    private String knowledgeCommentContent;
    private Timestamp knowledgeCommentCreatedAt;
    //업데이트한 시간 추가함
    private Timestamp knowledgeCommentUpdatedAt;

    //수정된 날짜 표기하는 메서드
    public Timestamp getDisplayDate(){
        return knowledgeCommentUpdatedAt !=null ? knowledgeCommentUpdatedAt : knowledgeCommentCreatedAt;
    }

    public boolean isUpdated(){
        return knowledgeCommentUpdatedAt !=null;
    }
    

}
