package com.devdotdone.ddd.dto.retrospec;

import java.time.LocalDateTime;

import lombok.Data;

/*
retro_id               NUMBER PRIMARY KEY,
project_id             NUMBER NOT NULL,
user_id                NUMBER NOT NULL,
retrospecTitle         VARCHAR2(100) NOT NULL,
retrospecContent       CLOB NOT NULL,
retrospecTemplateType  VARCHAR2(10), -- 회고 템플릿 종류: 'KTP','TIL','CSS'
retrospecCategory      VARCHAR2(20), -- 캘린더 category (time, allday, task, milestone)
retrospecStartAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
retrospecEndAt         TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 종료 시각 (null 가능)
 */

@Data
public class Retrospec {
    private int retroId;
    private int projectId;
    private int userId;              // 작성자
    private String retrospecTitle;   // 제목
    private String retrospecContent; // Editor
    private RetrospecTemplateType retrospecTemplateType;
    private String retrospecCategory;
    private LocalDateTime retrospecStartAt;
    private LocalDateTime retrospecEndAt;
}