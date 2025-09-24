package com.devdotdone.ddd.dto.knowledge;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Knowledge {
  private int knowledgeId;
  private int projectId;
  private int userId;
  private String knowledgeTitle;
  private String knowledgeContent;
  private String knowledgeUrl;

  private MultipartFile kfAttach;
  private String kfAttachoname;
  private String kfAttachsname;
  private String kfAttachtype;
  private byte[] kfAttachdata;

}
