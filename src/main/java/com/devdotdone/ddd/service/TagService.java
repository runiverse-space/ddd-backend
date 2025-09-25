package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.TagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagDao tagDao;

    public void insertTag(Tag tag) {
        tagDao.insertTag(tag);
    }

    public List<Tag> getTagByType(TagType tagType) {
        return tagDao.selectTagByType(tagType.name());
    }
}
