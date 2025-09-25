package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.tag.Tag;

@Mapper
public interface KnowledgeTagDao {
    void insertKnowledgeTag(@Param("knowledgeId") int knowledgeId, @Param("tagId") int tagId);
    int deleteKnowledgeTag(@Param("knowledgeId") int knowledgeId, @Param("tagId") int tagId);
    List<Tag> selectTagByKnowledgeId(@Param("knowledgeId") int knowledgeId);
}
