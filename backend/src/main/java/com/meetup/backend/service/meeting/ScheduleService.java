package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.entity.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by seongmin on 2022/10/25
 * updated by seongmin on 2022/11/14
 */
public interface ScheduleService {

    ScheduleResponseDto getScheduleDetail(String userId, Long scheduleId);

    AllScheduleResponseDto getScheduleByUserAndDate(String loginUserId, String targetUserId, String date);

    Long createSchedule(String userId, ScheduleRequestDto scheduleRequestDto);

    Long updateSchedule(String userId, ScheduleUpdateRequestDto scheduleUpdateRequestDto);

    void deleteSchedule(String userId, Long scheduleId);

    List<Schedule> getScheduleListWithLock(String date, String loginUserId, String targetUserId, int p);

    List<Schedule> getScheduleListWithoutLock(String date, String loginUserId, String targetUserId, int p);

    AllScheduleResponseDto getSchedule(String loginUserId, String targetUserId, String date, int p, List<Schedule> schedules);

    void diffDurationCheck(LocalDateTime start, LocalDateTime end);
}
