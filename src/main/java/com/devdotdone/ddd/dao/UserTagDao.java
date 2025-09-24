package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.tag.Tag;

@Mapper
public interface UserTagDao {
    // insertUserTag
    void insertUserTag(@Param("userId") int userId, @Param("tagId") int tagId);

    // selectTagByUserId
    List<Tag> selectTagByUserId(int userId);

    //deleteUserTag
    void deleteTagById(@Param("userId") int userId, @Param("tagId") int tagId);
}