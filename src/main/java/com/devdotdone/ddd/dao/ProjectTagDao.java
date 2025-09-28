package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.tag.Tag;

// import com.devdotdone.ddd.dto.tag.ProjectTag;

@Mapper
public interface ProjectTagDao {
    void insertProjectTag(@Param("projectId") int projectId, @Param("tagId") int tagId);
    int deleteProjectTag(@Param("projectId") int projectId, @Param("tagId") int tagId);
    List<Tag> selectTagByProjectId(@Param("projectId") int projectId);
}
