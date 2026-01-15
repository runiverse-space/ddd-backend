package com.devdotdone.ddd.dto.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ProjectResponse {
    private int projectId;
    private int userId;
    private String projectTitle;
    private String projectContent;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private LocalDateTime projectCreatedAt;
    private List<Integer> memberIds;
}
