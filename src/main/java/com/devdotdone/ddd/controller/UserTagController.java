package com.devdotdone.ddd.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;
import com.devdotdone.ddd.dto.tag.UserTag;
import com.devdotdone.ddd.service.UserTagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users/{userId}/tags")
public class UserTagController {
    @Autowired
    private UserTagService userTagService;

    @PostMapping
    public int createUserTag(@PathVariable("userId") int userId, @RequestBody Map<String, List<Integer>> body) {
         List<Integer> tagIds = body.get("tagIds");
        return userTagService.createUserTag(userId, tagIds);
    }

    @GetMapping
    public List<Tag> getUserTags(@PathVariable("userId") int userId) {
        return userTagService.getUserTag(userId);
    }

    @DeleteMapping
    public int deleteUserTag(@PathVariable("userId") int userId, @RequestBody Map<String, List<Integer>> body) {
        List<Integer> tagIds = body.get("tagIds");
        return userTagService.deleteUserTag(userId, tagIds);
    }

}
