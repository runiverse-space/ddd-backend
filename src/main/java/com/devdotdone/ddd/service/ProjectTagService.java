package com.devdotdone.ddd.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.ProjectTagDao;
import com.devdotdone.ddd.dto.tag.ProjectTagRequest;
import com.devdotdone.ddd.dto.tag.ProjectTagResponse;
import com.devdotdone.ddd.dto.tag.Tag;

@Service
public class ProjectTagService {
    @Autowired
    private ProjectTagDao projectTagDao;

    @Transactional
    public ProjectTagResponse update(ProjectTagRequest request) {
        List<Tag> currentTags = projectTagDao.selectTagByProjectId(request.getProjectId());
        Set<Integer> currentIds = new HashSet<>();
        for (Tag tag : currentTags)
            currentIds.add(tag.getTagId());

        List<Integer> newIds = request.getTagIds();
        int total = newIds.size();
        TagValidator.validate(0, total);

        for (int tagId : newIds) {
            if (!currentIds.contains(tagId)) {
                projectTagDao.insertProjectTag(request.getProjectId(), tagId);
            }
        }

        for (int tagId : currentIds) {
            if (!newIds.contains(tagId)) {
                projectTagDao.deleteProjectTag(request.getProjectId(), tagId);
            }
        }

        return getProjectTags(request.getProjectId());
    }

    public ProjectTagResponse getProjectTags(int projectId) {
        List<Tag> tags = projectTagDao.selectTagByProjectId(projectId);
        ProjectTagResponse response = new ProjectTagResponse();
        response.setProjectId(projectId);
        response.setTags(tags != null ? tags : new ArrayList<>());
        return response;
    }
}
