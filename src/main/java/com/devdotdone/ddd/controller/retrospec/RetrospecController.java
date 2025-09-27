package com.devdotdone.ddd.controller.retrospec;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.retrospec.RetrospecRequest;
import com.devdotdone.ddd.dto.retrospec.RetrospecResponse;
import com.devdotdone.ddd.service.RetrospecService;

@RestController
@RequestMapping("/api/retrospec")
public class RetrospecController implements RetrospecControllerDocs {
    @Autowired
    private RetrospecService retrospecService;

    @PostMapping
    public String create(@RequestBody RetrospecRequest request) {
        retrospecService.create(request);
        return "회고 작성 성공";
    }

    @GetMapping("{retroId}")
    public RetrospecResponse getById(@PathVariable("retroId") int retroId) {
        return retrospecService.getById(retroId);
    }

    @GetMapping("/project/{projectId}")
    public List<RetrospecResponse> getByProject(@PathVariable("projectId") int projectId) {
        return retrospecService.getByProject(projectId);
    }

    @PutMapping("/{retroId}")
    public String update(@PathVariable("retroId") int retroId, @RequestBody RetrospecRequest request) {
        retrospecService.update(retroId, request);
        return "회고 수정 성공";
    }

    @DeleteMapping("/{retroId}")
    public String delete(@PathVariable("retroId") int retroId,
                         @RequestParam("projectId") int projectId,
                         @RequestParam("userId") int userId) {
        retrospecService.delete(retroId, projectId, userId);
        return "회고 삭제 성공";
    }
}
