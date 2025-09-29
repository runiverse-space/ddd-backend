package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devdotdone.ddd.dto.retrospec.Retrospec;
import com.devdotdone.ddd.dto.retrospec.RetrospecRequest;

@Mapper
public interface RetrospecDao {
    void insertRetrospec(RetrospecRequest request);
    Retrospec selectByretroId(@Param("retroId") int retroId);
    List<Retrospec> selectByProject(@Param("projectId") int projectId);
    int updateRetrospec(RetrospecRequest request);
    // retroId + projectId + userId 일치해야 삭제
    int deleteRetrospec(@Param("retroId") int retroId,
                        @Param("projectId") int projectId,
                        @Param("userId") int userId);
}
