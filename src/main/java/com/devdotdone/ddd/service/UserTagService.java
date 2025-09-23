package com.devdotdone.ddd.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.TagDao;
import com.devdotdone.ddd.dao.UserTagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserTagService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private UserTagDao userTagDao;

    // createUserTags
    @Transactional
    public int createUserTag(int userId, List<Integer> tagIds) {
        // 선택한 태그 개수 조회
        int count = userTagDao.countByUserTag(userId);

        // 최대 3개 규칙 검증
        if (count + tagIds.size() > 3) {
            throw new IllegalArgumentException("태그는 최대 3개까지 선택할 수 있습니다.");
        }

        // 태그 유효성 체크
        List<Tag> allTags = tagDao.selectAllTags();

        for (Integer tagid : tagIds) {
            boolean exists = allTags.stream().anyMatch(tag -> tag.getTagId() == tagid);

            if (!exists) {
                throw new IllegalArgumentException("존재하지 않는 태그 ID: " + tagid);
            }
        }

        // DB에 isnert
        return userTagDao.insertUserTag(userId, tagIds);
    }

    // getUserTags
    public List<Tag> getUserTag(int userId) {
        return userTagDao.selectByUserTag(userId);
    }

    // 유저 태그 조회 (타입)
    public List<Tag> getUserTagByType(int userId, TagType tagType) {
        List<Tag> userTags = userTagDao.selectByUserTag(userId);

        // TagType enum 기준 필터링
        return userTags.stream()
                .filter(tag -> TagType.valueOf(tag.getTagType()) == tagType)
                .collect(Collectors.toList());
    }

    // deleteUserTags
    public int deleteUserTag(int userId, List<Integer> tagIds) {
        return userTagDao.deleteUserTag(userId, tagIds);
    }
}
