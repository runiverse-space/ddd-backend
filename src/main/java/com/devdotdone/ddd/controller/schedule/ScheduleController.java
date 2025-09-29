package com.devdotdone.ddd.controller.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devdotdone.ddd.dto.schedule.Schedule;
import com.devdotdone.ddd.dto.schedule.ScheduleRequest;
import com.devdotdone.ddd.dto.users.Users;
import com.devdotdone.ddd.service.ScheduleService;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController implements ScheduleControllerDocs {
  @Autowired
  private ScheduleService scheduleService;

  @PostMapping("/create")
  public Map<String, Object> create(@RequestBody ScheduleRequest request) {
    Map<String, Object> map = new HashMap<>();
    try {
      scheduleService.create(request);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @GetMapping("/detail")
  public Map<String, Object> detail(@RequestParam("scheduleId") int scheduleId) {
    Map<String, Object> map = new HashMap<>();
    Schedule schedule = scheduleService.getSchedule(scheduleId);
    try {
      map.put("result", "success");
      map.put("schedule", schedule);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }

  @GetMapping("/users")
  public Map<String, Object> users(@RequestParam("scheduleId") int scheduleId) {
    Map<String, Object> map = new HashMap<>();
    List<Users> usersList = scheduleService.getAssignedUsers(scheduleId);
    map.put("users", usersList);
    return map;
  }

  @PutMapping("/update")
  public Map<String, Object> update(@RequestBody ScheduleRequest request) {
    Map<String, Object> map = new HashMap<>();
    try {
      Schedule schedule = scheduleService.update(request);
      map.put("result", "success");
      map.put("data", schedule);
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }
    return map;
  }

  @DeleteMapping("/delete")
  public Map<String, Object> delete(@RequestParam("scheduleId") int scheduleId) {
    Map<String, Object> map = new HashMap<>();

    try {
      int row = scheduleService.remove(scheduleId);
      if (row == 1) {
        map.put("result", "success");
        map.put("message", "일정이 삭제되었습니다.");
      }
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("message", e.getMessage());
    }

    return map;
  }

}
