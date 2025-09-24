package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

@Data
public class UserTagResponse {
    private int userId;
    private List<Tag> tags; // 태그 정보
}
