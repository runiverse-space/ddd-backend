package com.devdotdone.ddd.dto.tag;

import java.util.List;

import lombok.Data;

/**
 * 유저 태그 응답 DTO
 * - 등록/삭제/조회 결과 반환
 */

@Data
public class UserTagResponse {
    private int userId;
    private List<Tag> tags; // 태그 정보
}
