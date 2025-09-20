package com.devdotdone.ddd.dto.knowledge;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Knowledge {
  private int projectId;
  private int userId;
  private String knowledgeTitle;
  private String knoledgeContent;
  private String knowledgeUrl;

  private MultipartFile battach;
  private String kfattachoname;
  private String kfattachsname;
  private String kfattachtype;
  private byte[] kfattachdata;

}
