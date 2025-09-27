package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.KnowledgeTagDao;
import com.devdotdone.ddd.dto.tag.KnowledgeTagRequest;
import com.devdotdone.ddd.dto.tag.KnowledgeTagResponse;
import com.devdotdone.ddd.dto.tag.Tag;

@Service
public class KnowledgeTagService {
    @Autowired
    private KnowledgeTagDao knowledgeTagDao;

    public KnowledgeTagResponse create(KnowledgeTagRequest request) {
        List<Tag> existing = knowledgeTagDao.selectTagByKnowledgeId(request.getKnowledgeId());
        TagValidator.validate(existing.size(), request.getTagIds().size());

        for (int tagId : request.getTagIds()) {
            boolean alreadyExists = existing.stream().anyMatch(t -> t.getTagId() == tagId);

            if (alreadyExists) {
                throw new IllegalArgumentException("이미 등록된 태그입니다.");
            }
            knowledgeTagDao.insertKnowledgeTag(request.getKnowledgeId(), tagId);
        }
        return getKnowledgeTags(request.getKnowledgeId());
    }

    public KnowledgeTagResponse delete(KnowledgeTagRequest request) {
        for (int tagId : request.getTagIds()) {
            int rows = knowledgeTagDao.deleteKnowledgeTag(request.getKnowledgeId(), tagId);

            if (rows == 0) {
                throw new IllegalArgumentException("삭제할 태그가 존재하지 않습니다.");
            }
        }
        return getKnowledgeTags(request.getKnowledgeId());
    }

    public KnowledgeTagResponse getKnowledgeTags(int knowledgeId) {
        List<Tag> tags = knowledgeTagDao.selectTagByKnowledgeId(knowledgeId);

        KnowledgeTagResponse response = new KnowledgeTagResponse();
        response.setKnowledgeId(knowledgeId);
        response.setTags(tags);

        return response;
    }
}
