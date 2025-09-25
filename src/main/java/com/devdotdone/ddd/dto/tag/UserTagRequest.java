package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

/**
 * 유저 태그 등록/삭제 요청 DTO
 * - JSON으로 { "userId": 1, "tagIds": [1,2,3] } 형태로 보냄
 */

@Data
public class UserTagRequest {
    private int userId;
    private List<Integer> tagIds; // 태그 선택 ID
}
