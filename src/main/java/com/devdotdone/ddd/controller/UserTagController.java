package com.devdotdone.ddd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.UserTagRequest;
import com.devdotdone.ddd.dto.tag.UserTagResponse;
import com.devdotdone.ddd.service.UserTagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userTag")
public class UserTagController {
    private final UserTagService userTagService;

    // 태그 조회
    @GetMapping("/{userId}")
    public UserTagResponse getUserTag(@PathVariable("userId") int userId) {
        List<Tag> tags = userTagService.getTags(userId);

        UserTagResponse res = new UserTagResponse();
        res.setUserId(userId);
        res.setTags(tags);
        return res;
    }

    // 태그 추가
    @PostMapping("/{userId}/tags/{tagId}")
    public void create(@PathVariable("userId") int userId, @PathVariable("tagId") int tagId) {
        userTagService.insertTag(userId, tagId);
    }

    // 태그 삭제
    @DeleteMapping("/{userId}/tags/{tagId}")
    public Map<String, String> delete(@PathVariable("userId") int userId, @PathVariable("tagId") int tagId) {
        userTagService.deleteTag(userId, tagId);

        Map<String, String> result = new HashMap<>();
        result.put("result", "성공");
        return result;
    }

    // 여러 태그 등록
    @PutMapping
    public Map<String, String> updateTags(@RequestBody UserTagRequest request) {
        userTagService.updateTag(request);

        Map<String, String> result = new HashMap<>();
        result.put("result", "성공");
        return result;
    }
}