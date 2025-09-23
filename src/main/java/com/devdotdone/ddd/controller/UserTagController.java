package com.devdotdone.ddd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;
import com.devdotdone.ddd.dto.tag.UserTagRequest;
import com.devdotdone.ddd.service.UserTagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/userTag")
public class UserTagController {
    @Autowired
    private UserTagService userTagService;

    // 사용자 태그 등록
    @PostMapping
    public int create(@RequestBody UserTagRequest request) {
        int userId = request.getUserId();
        List<Integer> tagIds = request.getTagIds();

        return userTagService.createUserTag(userId, tagIds);
    }

    // 전체 태그 조회
    @GetMapping("/{userId}")
    public List<Tag> getAllUserTags(@PathVariable("userId") int userId) {
        return userTagService.getUserTag(userId);
    }

    // 타입별 태그 조회 /{userId}/type?type={type}
    @GetMapping("/{userId}/type")
    public List<Tag> getUserTagsByType(@PathVariable("userId") int userId, @RequestParam("type") TagType type) {
        // type = USER, PROJECT, KNOWLEDGE
        return userTagService.getUserTagByType(userId, type);
    }

    // 사용자 태그 삭제
    @DeleteMapping
    public int delete(@RequestBody UserTagRequest request) {
        int userId = request.getUserId();
        List<Integer> tagIds = request.getTagIds();

        // DB에서 해당 태그 삭제
        return userTagService.deleteUserTag(userId, tagIds);
    }

}
