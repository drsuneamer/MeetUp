package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.util.converter.StringToLocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ChannelUserRepository channelUserRepository;
    private final ChannelRepository channelRepository;
    private final MeetupRepository meetupRepository;


    // 스케쥴의 ID로 일정 갖고 오기 (디테일)
    @Override
    public ScheduleResponseDto getScheduleResponseDtoById(String userId, Long scheduleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ApiException(ExceptionEnum.SCHEDULE_NOT_FOUND));
        if (!user.getId().equals(schedule.getUser().getId())) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);
        }
        return ScheduleResponseDto.builder().id(schedule.getId()).start(schedule.getStart()).end(schedule.getEnd()).title(schedule.getTitle()).content(schedule.getContent()).userId(user.getId()).userName(user.getNickname()).build();
    }

    // 로그인 한 유저의 일정 갖고오기
    @Override
    public List<ScheduleResponseDto> getScheduleResponseDtoByUserAndDate(String loginUserId, String date) {
        User loginUser = userRepository.findById(loginUserId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));

        LocalDateTime from = StringToLocalDateTime.strToLDT(date);
        LocalDateTime to = from.plusDays(6);
        List<Schedule> schedules = scheduleRepository.findAllByStartBetweenAndUser(from, to, loginUser);
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleResponseDtos.add(ScheduleResponseDto.of(schedule, loginUser));
        }
        return scheduleResponseDtos;
    }

    // 해당 user, mmetup, date로 정보 가져오기
    @Override
    public AllScheduleResponseDto getScheduleResponseDtoByUserAndDate(String loginUserId, Long meetupId, String date) {
        User loginUser = userRepository.findById(loginUserId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        Channel channel = meetup.getChannel();

        if (!channelUserRepository.existsByChannelAndUser(channel, loginUser))
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);

        LocalDateTime from = StringToLocalDateTime.strToLDT(date);
        LocalDateTime to = from.plusDays(6);
        List<Schedule> schedules = scheduleRepository.findAllByStartBetweenAndUser(from, to, meetup.getManager());
        return AllScheduleResponseDto.of(schedules);
    }

    // 스케쥴 정보 등록
    @Override
    @Transactional
    public void createSchedule(String userId, ScheduleRequestDto scheduleRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        LocalDateTime start = StringToLocalDateTime.strToLDT(scheduleRequestDto.getStart());
        LocalDateTime end = StringToLocalDateTime.strToLDT(scheduleRequestDto.getEnd());
        String title = scheduleRequestDto.getTitle();
        String content = scheduleRequestDto.getContent();
        Schedule schedule = new Schedule(start, end, title, content, user);
        scheduleRepository.save(schedule);
    }

    // 스케쥴 정보 수정
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

    // 스케쥴 정보 삭제
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
