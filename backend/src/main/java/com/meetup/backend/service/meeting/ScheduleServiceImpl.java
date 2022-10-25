package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public ScheduleResponseDto getScheduleResponseDtoById(String userId, Long scheduleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ApiException(ExceptionEnum.SCHEDULE_NOT_FOUND));
        if (!user.getId().equals(schedule.getUser().getId())) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);
        }
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .start(schedule.getStart())
                .end(schedule.getEnd())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .userId(user.getId())
                .userName(user.getNickname())
                .build();
    }

    @Override
    public List<ScheduleResponseDto> getScheduleResponseDtoByUserAnd(String userId, String date) {
        return null;
    }

    @Override
    public void createSchedule(String userId, ScheduleRequestDto scheduleRequestDto) {

    }

    @Override
    public void updateMeeting(String userId, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {

    }

    @Override
    public void deleteSchedule(String userId, Long ScheduleId) {

    }
}
