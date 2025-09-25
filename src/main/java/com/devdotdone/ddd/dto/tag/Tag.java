package com.devdotdone.ddd.dto.tag;

import lombok.Data;

/**
 * 태그 DTO
 * - DB tag 테이블과 매핑됨
 */

@Data
public class Tag {
  private int tagId;       // PK
  private String tagName;  // 태그명
  private TagType tagType; // Enum
}
