package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.tag.Tag;

@Mapper
public interface TagDao {
    // insertTag
    void insertTag(Tag tag);
    
    // selectTagById
    List<Tag> selectTagById(@Param("tagIds") List<Integer> tagIds);

    // selectAllTag
    List<Tag> selectAllTag();
}
