package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.TagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;

@Service
public class TagService {
    @Autowired
    private TagDao tagDao;

    public void insertTag(Tag tag) {
        tagDao.insertTag(tag);
    }

    public List<Tag> getTagByType(TagType tagType) {
        return tagDao.selectTagByType(tagType.name());
    }

    public List<Tag> searchTags(String keyword) {
        return tagDao.searchTags(keyword);
    }
}
