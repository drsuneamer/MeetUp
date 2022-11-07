package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;

import java.util.List;

/**
 * created by seongmin on 2022/10/25
 * updated by seongmin on 2022/11/07
 */
public interface ScheduleService {

    ScheduleResponseDto getScheduleDetail(String userId, Long scheduleId);

    AllScheduleResponseDto getScheduleByUserAndDate(String loginUserId, String targetUserId, String date);

    Long createSchedule(String userId, ScheduleRequestDto scheduleRequestDto);

    Long updateSchedule(String userId, ScheduleUpdateRequestDto scheduleUpdateRequestDto);

    void deleteSchedule(String userId, Long scheduleId);
}
