package com.devdotdone.ddd.dto.knowledge;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
/*
 CREATE TABLE knowledge (
    knowledge_id  NUMBER PRIMARY KEY,
    project_id    NUMBER NOT NULL,
    user_id     NUMBER NOT NULL,
    knowledgeTitle         VARCHAR2(100) NOT NULL,
    knowledgeContent       CLOB NOT NULL,
    knowledgeUrl           VARCHAR2(255), -- 참고 링크
    
    kfattachoname    varchar(100)  null, --파일
    kfattachsname    varchar(100)  null,
    kfattachtype     varchar(100)  null,
    kfattachdata     blob          NULL,
    knowledgeCreatedAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kn_project FOREIGN KEY (project_id) REFERENCES project(project_id),
    CONSTRAINT fk_kn_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);
 */


@Data
public class Knowledge {
  private int knowledgeId;
  private int projectId;
  private int userId;
  private String knowledgeTitle;
  private String knowledgeContent;
  private String knowledgeUrl;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private MultipartFile kfAttach;

  private String kfAttachoname;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String kfAttachsname;

  private String kfAttachtype;
  
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private byte[] kfAttachdata;

}
