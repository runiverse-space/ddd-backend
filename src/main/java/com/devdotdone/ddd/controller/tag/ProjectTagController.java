package com.devdotdone.ddd.controller.tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.ProjectTagRequest;
import com.devdotdone.ddd.dto.tag.ProjectTagResponse;
import com.devdotdone.ddd.service.ProjectTagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projectTag")
public class ProjectTagController implements ProjectTagControllerDocs {
    private final ProjectTagService projectTagService;

    @PostMapping("/create")
    public ProjectTagResponse create(@RequestBody ProjectTagRequest request) {
        return projectTagService.create(request);
    }

    @DeleteMapping("/delete")
    public ProjectTagResponse delete(@RequestBody ProjectTagRequest request) {
        return projectTagService.delete(request);
    }

    @GetMapping("/{projectId}")
    public ProjectTagResponse getProjectTags(@PathVariable("projectId") int projectId) {
        return projectTagService.getProjectTags(projectId);
    }
}