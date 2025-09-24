package com.devdotdone.ddd.dto.tag;

import lombok.Data;
/*
CREATE TABLE tag (
    tag_id        NUMBER PRIMARY KEY,
    tagName          VARCHAR2(50) NOT NULL,
    tagType          VARCHAR2(20)
); 
*/
@Data
public class Tag {
  private int tagId;
  private String tagName;
  private TagType tagType; // Enum
}
