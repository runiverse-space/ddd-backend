package com.devdotdone.ddd.dao;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.schedule.Schedule;

@Mapper
public interface ScheduleDao {
  public int insert(Schedule schedule);
  public Schedule selectScheduleById(int scheduleId);
  public int update(Schedule schedule);
  public int delete(int scheduleId);
}
