package com.devdotdone.ddd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devdotdone.ddd.dto.schedule.ScheduleMember;

@Mapper
public interface ScheduleMemberDao {
  public void chargeUsers(ScheduleMember scheduleMember);
  public List<ScheduleMember> findUsers(int scheduleId);
  public int dischargeUsers(int userId);
}
