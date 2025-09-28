package com.devdotdone.ddd.service;

/**
 * 공통 검증 클래스
 * 태그는 최대 3개까지만 등록 가능
 */

public class TagValidator {
    private static final int MAX_TAGS = 3;

    public static void validate(int existingCount, int newCount) {
        if (existingCount + newCount > MAX_TAGS) {
            throw new IllegalArgumentException("태그는 최대 " + MAX_TAGS + "개까지 선택 가능합니다.");
        }
    }
}
