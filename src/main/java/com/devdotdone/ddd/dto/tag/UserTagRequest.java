package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

@Data
public class UserTagRequest {
    private int userId;
    private List<Integer> tagIds;
}
