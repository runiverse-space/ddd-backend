package com.devdotdone.ddd.dto.project;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ProjectRequest {
    private int uprId;
    private int userId;
    private int projectId;

    private String uprRole;

    private String projectTitle;
    private String projectContent;
    private Date projectStartDate;
    private Date projectEndDate;
    private Date projectCreatedAt;

    private List<Integer> userIds;
    private List<Integer> removeUserIdList;

    private List<ProjectMilestone> projectMilestones;
}
