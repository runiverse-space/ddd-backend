package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.tag.Tag;

@Mapper
public interface UserTagDao {
    // insertUserTags
    int insertUserTag(@Param("userId") int userId, @Param("tagIds") List<Integer> tagIds);

    // selectTagsByUser
    List<Tag> selectByUserTag(int userId);

    // deleteUserTags
    int deleteUserTag(@Param("userId") int userId, @Param("tagIds") List<Integer> tagIds);

    // countByUserTag
    int countByUserTag(int userId);    
}
