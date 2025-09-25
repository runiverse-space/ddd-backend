package com.devdotdone.ddd.controller.tag;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.devdotdone.ddd.dto.tag.Tag;
import com.devdotdone.ddd.dto.tag.TagType;

import io.swagger.v3.oas.annotations.Operation;

// @Tag(name="태그", description = "태그 관련 API")
@io.swagger.v3.oas.annotations.tags.Tag(name = "태그", description = "태그 관련 API")
public interface TagControllerDocs {
    @Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다")
    void insertTag(@RequestBody Tag tag);

    @Operation(summary = "태그 조회", description = "태그 타입별로 목록을 조회합니다")
    List<Tag> getTagByType(@PathVariable("tagType") TagType tagType);
}