package com.devdotdone.ddd.controller.schedule;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.devdotdone.ddd.dto.schedule.ScheduleRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="일정", description = "일정 관련 API")
public interface ScheduleControllerDocs {

  @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다")
  Map<String, Object> create(@RequestBody ScheduleRequest request);

  @Operation(summary = "일정 조회", description = "일정의 아이디로 일정을 조회합니다")
  Map<String, Object> detail(@RequestParam("scheduleId") int scheduleId);

  @Operation(summary = "일정을 배정받은 사용자 목록 조회", description = "일정을 배정받은 사용자 목록을 조회합니다")
  Map<String, Object> users(@RequestParam("scheduleId") int scheduleId);

  @Operation(summary = "일정 수정", description = "일정을 수정합니다")
  Map<String, Object> update(@RequestBody ScheduleRequest request);

  @Operation(summary = "일정 삭제", description = "일정을 삭제합니다")
  Map<String, Object> delete(@RequestParam("scheduleId") int scheduleId);
}
