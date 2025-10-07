package com.devdotdone.ddd.controller.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.UserTagRequest;
import com.devdotdone.ddd.dto.tag.UserTagResponse;
import com.devdotdone.ddd.service.UserTagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/userTag")
public class UserTagController {
    @Autowired
    private UserTagService userTagService;

    @PostMapping("/update")
    public UserTagResponse update(@RequestBody UserTagRequest request) {
        return userTagService.update(request);
    }

    @GetMapping("/{userId}")
    public UserTagResponse getUserTags(@PathVariable("userId") int userId) {
        return userTagService.getUserTags(userId);
    }
}