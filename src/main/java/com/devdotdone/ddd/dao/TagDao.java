package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.tag.Tag;
/**
 * 태그 DAO
 * - 태그 등록
 * - 타입별 태그 조회
 */
@Mapper
public interface TagDao {
    void insertTag(Tag tag);
    List<Tag> selectTagByType(@Param("tagType") String tagType);
    List<Tag> searchTags(String keyword);
}
