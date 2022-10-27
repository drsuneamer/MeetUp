package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.Schedule.ScheduleRequestDto;
import com.meetup.backend.dto.Schedule.ScheduleResponseDto;
import com.meetup.backend.dto.Schedule.ScheduleUpdateRequestDto;

import java.util.List;

/**
 * created by seongmin on 2022/10/25
 */
public interface ScheduleService {

    ScheduleResponseDto getScheduleResponseDtoById(String userId, Long scheduleId);

    List<ScheduleResponseDto> getScheduleResponseDtoByUserAndDate(String userId, String date);

    void createSchedule(String userId, ScheduleRequestDto scheduleRequestDto);

    void updateSchedule(String userId, ScheduleUpdateRequestDto scheduleUpdateRequestDto);

    void deleteSchedule(String userId, Long scheduleId);
}
