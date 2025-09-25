package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

@Data
public class ProjectTagResponse {
    private int projectId;
    private List<Tag> tags;
}
