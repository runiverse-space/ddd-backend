package com.devdotdone.ddd.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devdotdone.ddd.dao.RetrospecDao;
import com.devdotdone.ddd.dto.retrospec.Retrospec;
import com.devdotdone.ddd.dto.retrospec.RetrospecRequest;
import com.devdotdone.ddd.dto.retrospec.RetrospecResponse;

@Service
public class RetrospecService {
    @Autowired
    private RetrospecDao retrospecDao;

    @Transactional
    public void create(RetrospecRequest request) {
        retrospecDao.insertRetrospec(request);
    }

    public RetrospecResponse getById(int retroId) {
        Retrospec retrospec = retrospecDao.selectByretroId(retroId);
        if (retrospec == null) {
            // 조회 결과 없으면 null 반환
            throw new IllegalArgumentException("회고를 찾을 수 없습니다.");
        }
        return toResponse(retrospec);
    }

    public List<RetrospecResponse> getByProject(int projectId) {
        List<RetrospecResponse> list = new ArrayList<>();
        for (Retrospec retro : retrospecDao.selectByProject(projectId)) {
            list.add(toResponse(retro));
        }
        return list;
    }

    @Transactional
    public void update(int retroId, RetrospecRequest request) {
        request.setRetroId(retroId);
        int rows = retrospecDao.updateRetrospec(request);
        if (rows == 0) {
            throw new IllegalArgumentException("회고 수정 권한이 없거나 회고가 존재하지 않습니다.");
        }
    }

    @Transactional
    public void delete(int retroId, int projectId, int userId) {
        int rows = retrospecDao.deleteRetrospec(retroId, projectId, userId);
        if (rows == 0) {
            throw new IllegalArgumentException("회고 삭제 권한이 없거나 회고가 존재하지 않습니다.");
        }
    }

    private RetrospecResponse toResponse(Retrospec retrospec) {
        RetrospecResponse response = new RetrospecResponse();
        response.setRetroId(retrospec.getRetroId());
        response.setProjectId(retrospec.getProjectId());
        response.setUserId(retrospec.getUserId());
        response.setRetrospecTitle(retrospec.getRetrospecTitle());
        response.setRetrospecContent(retrospec.getRetrospecContent());
        response.setRetrospecTemplateType(retrospec.getRetrospecTemplateType());
        response.setRetrospecCategory(retrospec.getRetrospecCategory());
        response.setRetrospecStartAt(retrospec.getRetrospecStartAt());
        response.setRetrospecEndAt(retrospec.getRetrospecEndAt());
        return response;
    }
}
