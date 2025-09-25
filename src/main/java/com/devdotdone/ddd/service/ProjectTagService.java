package com.devdotdone.ddd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devdotdone.ddd.dao.ProjectTagDao;
import com.devdotdone.ddd.dto.tag.ProjectTagRequest;
import com.devdotdone.ddd.dto.tag.ProjectTagResponse;
import com.devdotdone.ddd.dto.tag.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectTagService {

    private final ProjectTagDao projectTagDao;

    // 태그 등록
    public ProjectTagResponse create(ProjectTagRequest request) {
        List<Tag> existing = projectTagDao.selectTagByProjectId(request.getProjectId());
        TagValidator.validate(existing.size(), request.getTagIds().size());
        
        for (int tagId : request.getTagIds()) {
            boolean alreadyExists = existing.stream().anyMatch(t -> t.getTagId() == tagId);
            
            if (alreadyExists) {
                throw new IllegalArgumentException("이미 등록된 태그입니다.");
            }
            projectTagDao.insertProjectTag(request.getProjectId(), tagId);
        }
        return getProjectTags(request.getProjectId());
    }

    // 태그 삭제
    public ProjectTagResponse delete(ProjectTagRequest request) {
        for (int tagId : request.getTagIds()) {
            int rows = projectTagDao.deleteProjectTag(request.getProjectId(), tagId);
            
            if (rows == 0) {
                throw new IllegalArgumentException("삭제할 태그가 존재하지 않습니다.");
            }
        }
        return getProjectTags(request.getProjectId());
    }

    // 태그 조회
    public ProjectTagResponse getProjectTags(int projectId) {
        List<Tag> tags = projectTagDao.selectTagByProjectId(projectId);

        ProjectTagResponse response = new ProjectTagResponse();
        response.setProjectId(projectId);
        response.setTags(tags);
        
        return response;
    }

}
