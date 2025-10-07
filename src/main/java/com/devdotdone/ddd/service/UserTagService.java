package com.devdotdone.ddd.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.UserTagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.UserTagRequest;
import com.devdotdone.ddd.dto.tag.UserTagResponse;

/**
 * 유저 태그 서비스
 * - create/delete: update() 단일 메서드로 추가/삭제 동시 처리
 * - 항상 최종 상태(UserTagResponse) 반환
 */

@Service
public class UserTagService {
    @Autowired
    private UserTagDao userTagDao;

    @Transactional
    public UserTagResponse update(UserTagRequest request) {
        // 현재 등록된 태그 목록 조회
        List<Tag> currentTags = userTagDao.selectTagByUserId(request.getUserId());

        // 기존 태그의 id만 저장
        Set<Integer> currentIds = new HashSet<>();
        for (Tag tag : currentTags) {
            currentIds.add(tag.getTagId());
        }

        // 새로 선택된 태그 목록
        List<Integer> newIds = request.getTagIds();

        // 태그 개수 검증
        int totalCount = newIds.size();
        TagValidator.validate(0, totalCount);

        // 새로 추가된 태그 insert (기존에 없을 경우)
        for (int tagId : newIds) {
            if (!currentIds.contains(tagId)) {
                userTagDao.insertUserTag(request.getUserId(), tagId);
            }
        }

        // 빠진 태그 delete (기존에 있고, 새 목록에 없을 경우)
        for (int tagId : currentIds) {
            if (!newIds.contains(tagId)) {
                userTagDao.deleteUserTag(request.getUserId(), tagId);
            }
        }

        // 최종 상태 반환
        return getUserTags(request.getUserId());
    }

    public UserTagResponse getUserTags(int userId) {
        List<Tag> tags = userTagDao.selectTagByUserId(userId);

        UserTagResponse response = new UserTagResponse();
        response.setUserId(userId);
        response.setTags(tags != null ? tags : new ArrayList<>());
        return response;
    }

}