package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.UserTagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.UserTagRequest;
import com.devdotdone.ddd.dto.tag.UserTagResponse;

/**
 * 유저 태그 서비스
 * - 요청 받은 tagIds를 추가/삭제
 * - 항상 최종 상태(UserTagResponseDto) 반환
 */

@Service
public class UserTagService {
    @Autowired
    private UserTagDao userTagDao;

    public UserTagResponse create(UserTagRequest request) {
        // 현재 등록된 태그 목록 조회
        List<Tag> existing = userTagDao.selectTagByUserId(request.getUserId());

        // 기존 태그 개수 + 새 태그 개수 검증
        TagValidator.validate(existing.size(), request.getTagIds().size());

        for (int tagId : request.getTagIds()) {
            boolean alreadyExists = false;

            // 기존 태그들 중에서 동일한 tagId가 있는지 직접 for문으로 검사
            for (Tag t : existing) {
                if (t.getTagId() == tagId) {
                    alreadyExists = true;
                    break; // 동일한 태그가 하나라도 있다면
                }
            }

            // 이미 등록된 경우 예외 발생
            if (alreadyExists) {
                throw new IllegalArgumentException("이미 등록된 태그입니다. tagId=" + tagId);
            }

            // 새로운 태그만 insert
            userTagDao.insertUserTag(request.getUserId(), tagId);
        }

        // 최종 상태 반환
        return getUserTags(request.getUserId());
    }

    @Transactional
    public UserTagResponse delete(UserTagRequest request) {
        // 삭제할 tagIds를 받아 for문 돌며 deleteUserTag 실행
        for (int tagId : request.getTagIds()) {
            int rows = userTagDao.deleteUserTag(request.getUserId(), tagId);

            if (rows == 0) {
                throw new IllegalArgumentException("삭제할 태그가 존재하지 않습니다.");
            }
        }

        // 삭제 후 최종 상태 반환
        return getUserTags(request.getUserId());
    }

    public UserTagResponse getUserTags(int userId) {
        // db에서 해당 유저 태그 조회
        List<Tag> tags = userTagDao.selectTagByUserId(userId);

        // response에 userId와 List<Tag>를 담아서 반환
        UserTagResponse response = new UserTagResponse();
        response.setUserId(userId);
        response.setTags(tags);
        return response;
    }

}