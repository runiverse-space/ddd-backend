package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

@Data
public class ProjectTagRequest {
    private int projectId;
    List<Integer> tagIds;
}
