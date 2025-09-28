package com.devdotdone.ddd.dto.retrospec;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RetrospecRequest {
    private int retroId;
    private int projectId;
    private int userId;
    private String retrospecTitle;
    private String retrospecContent;
    private RetrospecTemplateType retrospecTemplateType;
    private String retrospecCategory;
    private LocalDateTime retrospecStartAt;
    private LocalDateTime retrospecEndAt;
}
