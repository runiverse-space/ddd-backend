package com.devdotdone.ddd.controller.retrospec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class RetrospecController {
    @Autowired
    private RetrospecService retrospecService;

    @PostMapping
    public ResponseEntity<RetrospecRequest> create(@RequestBody RetrospecRequest request) {
        retrospecService.create(request);
        return ResponseEntity.ok(request);
    }

    @GetMapping("{retroId}")
    public ResponseEntity<RetrospecResponse> getById(@PathVariable("retroId") int retroId) {
        RetrospecResponse response = retrospecService.getById(retroId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}")
    public List<RetrospecResponse> getByProject(@PathVariable("projectId") int projectId) {
        return retrospecService.getByProject(projectId);
    }

    @PutMapping("/{retroId}")
    public ResponseEntity<RetrospecRequest> update(@PathVariable("retroId") int retroId, @RequestBody RetrospecRequest request) {
        retrospecService.update(retroId, request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{retroId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable("retroId") int retroId,
                         @RequestParam("projectId") int projectId,
                         @RequestParam("userId") int userId) {
        retrospecService.delete(retroId, projectId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("retroId", retroId);
        response.put("message", "회고 삭제 성공");
        return ResponseEntity.ok(response);
    }
}
