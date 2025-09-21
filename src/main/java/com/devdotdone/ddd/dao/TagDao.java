package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.tag.Tag;

@Mapper
public interface TagDao {
  public int insertTag(Tag tag);

  public Tag selectTagById(int tagId);

  public Tag selectTagByTagName(String tagName);

  public Tag selectTagByTagType(String tagType);

  public List<Tag> selectAllTags();

  public int updateTag(Tag tag);

  public int deleteTag(String tagId);
}
