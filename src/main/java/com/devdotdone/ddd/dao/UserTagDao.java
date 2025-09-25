package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.tag.Tag;

@Mapper
public interface UserTagDao {
    void insertUserTag(@Param("userId") int userId, @Param("tagId") int tagId);
    int deleteUserTag(@Param("userId") int userId, @Param("tagId") int tagId);
    List<Tag> selectTagByUserId(@Param("userId") int userId);

}