package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.UserTagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;
import com.devdotdone.ddd.dto.tag.UserTagRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTagService {

    private final UserTagDao userTagDao;
    private final TagValidator tagValidator;

    // 태그 추가
    public void insertTag(int userId, int tagId) {
        try{
            tagValidator.validate(List.of(tagId), TagType.USER);
            userTagDao.insertUserTag(userId, tagId);
        }catch (Exception e){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

    // 태그 조회
    public List<Tag> getTags(int userId) {
        return userTagDao.selectTagByUserId(userId);
    }

    // 여러 태그 추가
    @Transactional
    public void updateTag(UserTagRequest request) {
        tagValidator.validate(request.getTagIds(), TagType.USER);
        request.getTagIds().forEach(tagId -> userTagDao.insertUserTag(request.getUserId(), tagId));
    }
    
    // 태그 삭제
    public void deleteTag(int userId, int tagId) {
        userTagDao.deleteTagById(userId, tagId);
    }
}