package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;

import java.util.List;

/**
 * created by seongmin on 2022/10/25
 * updated by seongmin on 2022/10/31
 */
public interface ScheduleService {

    ScheduleResponseDto getScheduleResponseDtoById(String userId, Long scheduleId);

    AllScheduleResponseDto getScheduleResponseDtoByUserAndDate(String loginUserId, String date);

    AllScheduleResponseDto getScheduleResponseDtoByUserAndDate(String loginUserId, Long meetupId, String date);

    void createSchedule(String userId, ScheduleRequestDto scheduleRequestDto);

    void updateSchedule(String userId, ScheduleUpdateRequestDto scheduleUpdateRequestDto);

    void deleteSchedule(String userId, Long scheduleId);
}
