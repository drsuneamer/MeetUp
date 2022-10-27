package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.Schedule.ScheduleRequestDto;
import com.meetup.backend.dto.Schedule.ScheduleResponseDto;
import com.meetup.backend.dto.Schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.util.converter.StringToLocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    private final StringToLocalDateTime STLD;

    // 스케쥴의 ID로 일정 갖고 오기 (디테일)
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

    // 해당 user와 date로 정보 가져오기
    @Override
    public List<ScheduleResponseDto> getScheduleResponseDtoByUserAndDate(String userId, String date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));

        LocalDateTime from = STLD.strToLDT(date);
        LocalDateTime to = from.plusDays(6);
        List<Schedule> schedules = scheduleRepository.findAllByStartBetweenAndUser(from, to, user);
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleResponseDtos.add(ScheduleResponseDto.of(schedule, user));
        }
        return scheduleResponseDtos;
    }

    @Override
    @Transactional
    public void createSchedule(String userId, ScheduleRequestDto scheduleRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        LocalDateTime start = STLD.strToLDT(scheduleRequestDto.getStart());
        LocalDateTime end = STLD.strToLDT(scheduleRequestDto.getEnd());
        String title = scheduleRequestDto.getTitle();
        String content = scheduleRequestDto.getContent();
        Schedule schedule = new Schedule(start, end, title, content, user);
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void updateSchedule(String userId, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleUpdateRequestDto.getId()).orElseThrow(() -> new ApiException(ExceptionEnum.SCHEDULE_NOT_FOUND));
        if (!user.getId().equals(schedule.getUser().getId())) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);
        }
        schedule.update(scheduleUpdateRequestDto);
    }

    @Override
    @Transactional
    public void deleteSchedule(String userId, Long scheduleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ApiException(ExceptionEnum.SCHEDULE_NOT_FOUND));
        if (!user.getId().equals(schedule.getUser().getId())) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);
        }
        scheduleRepository.delete(schedule);

    }
}
