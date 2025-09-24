package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.schedule.Schedule;

@Mapper
public interface ScheduleDao {
  public int insertSchedule(Schedule schedule);
  public Schedule selectScheduleById(int scheduleId);
  public List<Schedule> selectScheduleByProjectId(int projectId);
  public int updateSchedule(Schedule schedule);
  public int deleteSchedule(int scheduleId);
}
