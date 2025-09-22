package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.UserTagDao;
import com.devdotdone.ddd.dto.tag.Tag;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserTagService {
    @Autowired
    private UserTagDao userTagDao;

    // createUserTags
    public int createUserTag(int userId, List<Integer> tagIds) {
        return userTagDao.insertUserTag(userId, tagIds);
    }

    // getUserTags
    public List<Tag> getUserTag(int userId) {
        return userTagDao.selectByUserTag(userId);
    }

    // deleteUserTags
    public int deleteUserTag(int userId, List<Integer> tagIds) {
        return userTagDao.deleteUserTag(userId, tagIds);
    }
}
