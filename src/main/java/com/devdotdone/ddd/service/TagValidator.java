package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.devdotdone.ddd.dao.TagDao;
import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagValidator {
    private final TagDao tagDao;
    private static final int MAX_TAGS = 3;

    public void validate(List<Integer> tagIds, TagType expectedType) {
        if (tagIds.size() > MAX_TAGS) {
            throw new IllegalArgumentException("최대 3개까지 선택 가능합니다.");
        }

        List<Tag> tags = tagDao.selectTagById(tagIds);

        if (tags.size() != tagIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 태그가 포함되어 있습니다.");
        }

        for (Tag tag : tags) {
            if (tag.getTagType() != expectedType)
                throw new IllegalArgumentException("태그 타입이 올바르지 않습니다.");
        }
    }
}
