package com.devdotdone.ddd.controller.tag;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;
import com.devdotdone.ddd.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController implements TagControllerDocs{

    private final TagService tagService;

    @PostMapping("/create")
    public void insertTag(@RequestBody Tag tag) {
        tagService.insertTag(tag);
    }

    @GetMapping("/{tagType}")
    public List<Tag> getTagByType(@PathVariable("tagType") TagType tagType) {
        return tagService.getTagByType(tagType);
    }

}
