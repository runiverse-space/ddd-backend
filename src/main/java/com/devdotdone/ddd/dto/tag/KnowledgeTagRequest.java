package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

@Data
public class KnowledgeTagRequest {
    private int knowledgeId;
    List<Integer> tagIds;
}
