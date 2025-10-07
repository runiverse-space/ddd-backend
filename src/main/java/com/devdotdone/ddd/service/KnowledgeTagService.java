package com.devdotdone.ddd.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.KnowledgeTagDao;
import com.devdotdone.ddd.dto.tag.KnowledgeTagRequest;
import com.devdotdone.ddd.dto.tag.KnowledgeTagResponse;
import com.devdotdone.ddd.dto.tag.Tag;

@Service
public class KnowledgeTagService {
    @Autowired
    private KnowledgeTagDao knowledgeTagDao;

    @Transactional
    public KnowledgeTagResponse update(KnowledgeTagRequest request) {
        List<Tag> currentTags = knowledgeTagDao.selectTagByKnowledgeId(request.getKnowledgeId());
        Set<Integer> currentIds = new HashSet<>();
        for (Tag tag : currentTags)
            currentIds.add(tag.getTagId());

        List<Integer> newIds = request.getTagIds();
        int total = newIds.size();
        TagValidator.validate(0, total);

        for (int tagId : newIds) {
            if (!currentIds.contains(tagId)) {
                knowledgeTagDao.insertKnowledgeTag(request.getKnowledgeId(), tagId);
            }
        }

        for (int tagId : currentIds) {
            if (!newIds.contains(tagId)) {
                knowledgeTagDao.deleteKnowledgeTag(request.getKnowledgeId(), tagId);
            }
        }

        return getKnowledgeTags(request.getKnowledgeId());
    }

    public KnowledgeTagResponse getKnowledgeTags(int knowledgeId) {
        List<Tag> tags = knowledgeTagDao.selectTagByKnowledgeId(knowledgeId);
        KnowledgeTagResponse response = new KnowledgeTagResponse();
        response.setKnowledgeId(knowledgeId);
        response.setTags(tags != null ? tags : new ArrayList<>());
        return response;
    }
}
