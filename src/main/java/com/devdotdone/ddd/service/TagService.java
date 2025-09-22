package com.devdotdone.ddd.service;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.TagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TagService {
    @Autowired
    TagDao tagDao;

    public int createTag(Tag tag) {
        return tagDao.insertTag(tag);
    }

    public List<Tag> getAllTags() {
        return tagDao.selectAllTags();
    }

    public List<Tag> getTagType(TagType tagType) {
        return tagDao.selectByTagType(tagType.name());
    }

    public int updateTag(Tag tag) {
        int rows = tagDao.updateTag(tag);
        if (rows == 0) {
            throw new RuntimeException("해당 tagId가 존재하지 않습니다: " + tag.getTagId());
        }
        return rows;
    }

    public int deleteTag(int tagId) {
        return tagDao.deleteTag(tagId);
    }
}
