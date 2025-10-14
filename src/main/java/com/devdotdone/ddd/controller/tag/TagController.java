package com.devdotdone.ddd.controller.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;
import com.devdotdone.ddd.service.TagService;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<Tag> insertTag(@RequestBody Tag tag) {
        tagService.insertTag(tag);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/{tagType}")
    public List<Tag> getTagByType(@PathVariable("tagType") TagType tagType) {
        return tagService.getTagByType(tagType);
    }

    @GetMapping("/search")
    public List<Tag> searchTags(@RequestParam("keyword") String keyword) {
        return tagService.searchTags(keyword);
    }
}
