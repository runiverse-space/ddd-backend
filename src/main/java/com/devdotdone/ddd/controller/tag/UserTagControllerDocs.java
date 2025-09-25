package com.devdotdone.ddd.controller.tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.devdotdone.ddd.dto.tag.UserTagRequest;
import com.devdotdone.ddd.dto.tag.UserTagResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유저 태그", description = "유저 태그 관련 API")
public interface UserTagControllerDocs {
    @Operation(summary = "유저 태그 등록", description = "유저가 태그를 등록합니다")
    UserTagResponse create(@RequestBody UserTagRequest request);

    @Operation(summary = "유저 태그 삭제", description = "유저가 등록한 태그를 삭제합니다")
    UserTagResponse delete(@RequestBody UserTagRequest request);

    @Operation(summary = "유저 태그 조회", description = "유저가 등록한 태그 목록을 조회합니다")
    public UserTagResponse getUserTags(@PathVariable("userId") int userId);
}
