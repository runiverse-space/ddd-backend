package com.devdotdone.ddd.controller.tag;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유저 태그 컨트롤러
 * - 프론트에서 JSON으로 userId + tagIds를 받아 서비스 호출
 * - 결과는 ResponseDto(JSON)로 반환
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userTag")
public class UserTagController implements UserTagControllerDocs{
    private final UserTagService userTagService;

    @PostMapping("/create")
    public UserTagResponse create(@RequestBody UserTagRequest request) {
        return userTagService.create(request);
    }

    @DeleteMapping("/delete")
    public UserTagResponse delete(@RequestBody UserTagRequest request) {
        return userTagService.delete(request);
    }

    @GetMapping("/{userId}")
    public UserTagResponse getUserTags(@PathVariable("userId") int userId) {
        return userTagService.getUserTags(userId);
    }
}