package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.schedule.MeetingRepository;
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

import static com.meetup.backend.exception.ExceptionEnum.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ChannelUserRepository channelUserRepository;
    private final MeetupRepository meetupRepository;
    private final MeetingRepository meetingRepository;


    // 스케쥴의 ID로 일정 갖고 오기 (디테일)
    @Override
    public ScheduleResponseDto getScheduleResponseDtoById(String userId, Long scheduleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ApiException(SCHEDULE_NOT_FOUND));
        if (!user.getId().equals(schedule.getUser().getId())) {
            throw new ApiException(ACCESS_DENIED);
        }
        return ScheduleResponseDto.builder().id(schedule.getId()).start(schedule.getStart()).end(schedule.getEnd()).title(schedule.getTitle()).content(schedule.getContent()).userId(user.getId()).userName(user.getNickname()).build();
    }

    // 해당 user, 캘린더 주인 id, date로 정보 가져오기
    @Override
    public AllScheduleResponseDto getScheduleByUserAndDate(String loginUserId, String targetUserId, String date) {
        User loginUser = userRepository.findById(loginUserId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        List<Meetup> meetups = meetupRepository.findByManager(targetUser);

        boolean flag = false;
        for (Meetup meetup : meetups) {
            if (channelUserRepository.existsByChannelAndUser(meetup.getChannel(), loginUser)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new ApiException(ACCESS_DENIED_THIS_SCHEDULE);
        }

        LocalDateTime from = StringToLocalDateTime.strToLDT(date);
        LocalDateTime to = from.plusDays(6);
        List<Schedule> schedules = scheduleRepository.findAllByStartBetweenAndUser(from, to, targetUser);

        // 해당 스케줄 주인의 밋업 리스트
        List<Meetup> meetupList = meetupRepository.findByManager(targetUser);
        List<Meeting> meetingToMe = new ArrayList<>();
        if (meetupList.size() > 0) {
            for (Meetup mu : meetupList) {
                // 스케줄 주인이 신청 받은 미팅(컨,프,코,교 시점)
                meetingToMe.addAll(meetingRepository.findByMeetup(mu));
            }
        }
        return AllScheduleResponseDto.of(schedules, meetingToMe);
    }

    // 스케쥴 정보 등록
    @Override
    @Transactional
    public Long createSchedule(String userId, ScheduleRequestDto scheduleRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        LocalDateTime start = StringToLocalDateTime.strToLDT(scheduleRequestDto.getStart());
        LocalDateTime end = StringToLocalDateTime.strToLDT(scheduleRequestDto.getEnd());
        String title = scheduleRequestDto.getTitle();
        String content = scheduleRequestDto.getContent();
        Schedule schedule = new Schedule(start, end, title, content, user);

        return scheduleRepository.save(schedule).getId();
    }

    // 스케쥴 정보 수정
    @Override
    @Transactional
    public Long updateSchedule(String userId, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleUpdateRequestDto.getId()).orElseThrow(() -> new ApiException(SCHEDULE_NOT_FOUND));
        if (!user.getId().equals(schedule.getUser().getId())) {
            throw new ApiException(ACCESS_DENIED);
        }
        schedule.update(scheduleUpdateRequestDto);
        return schedule.getId();
    }

    // 스케쥴 정보 삭제
    @Override
    @Transactional
    public void deleteSchedule(String userId, Long scheduleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ApiException(SCHEDULE_NOT_FOUND));
        if (!user.getId().equals(schedule.getUser().getId())) {
            throw new ApiException(ACCESS_DENIED);
        }
        scheduleRepository.delete(schedule);

    }
}
