package com.devdotdone.ddd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;
import com.devdotdone.ddd.service.TagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping("/create")
    public int createTag(@RequestBody Tag tag) {
        log.info(tag.toString());
        return tagService.createTag(tag);
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    // @PathVariable + enum 자동 변환
    @GetMapping("/{tagType}")
    public List<Tag> getTagType(@PathVariable("tagType") TagType tagType) {
        return tagService.getTagType(tagType);
    }

    @PutMapping("/{tagId}")
    public int updateTag(@PathVariable("tagId") int tagId, @RequestBody Tag tag) {
        tag.setTagId(tagId);
        return tagService.updateTag(tag);
    }

    @DeleteMapping("/{tagId}")
    public int deleteTag(@PathVariable("tagId") int tagId) {
        return tagService.deleteTag(tagId);
    }

}
