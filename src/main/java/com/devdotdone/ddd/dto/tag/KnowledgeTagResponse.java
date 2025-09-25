package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

@Data
public class KnowledgeTagResponse {
    int knowledgeId;
    private List<Tag> tags;
}
