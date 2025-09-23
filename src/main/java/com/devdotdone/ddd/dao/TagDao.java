package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.tag.Tag;

/*
CREATE TABLE tag (
    tag_id        NUMBER PRIMARY KEY,
    tagName          VARCHAR2(50) NOT NULL,
    tagType          VARCHAR2(20)
); 
*/
@Mapper
public interface TagDao {
    // insertTag
    public int insertTag(Tag tag);

    // selectAllTags
    public List<Tag> selectAllTags();

    // selectTagByTagType
    public List<Tag> selectByTagType(String tagType);

    // updateTag
    public int updateTag(Tag tag);

    // deleteTag
    public int deleteTag(int tagId);
}
