package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.CalendarResponseDto;
import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.meetup.MeetupResponseDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.user.UserRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * created by seungyong on 2022/10/24
 * updated by seungyong on 2022/11/01
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetupServiceImpl implements MeetupService {

    private final ChannelRepository channelRepository;

    private final UserRepository userRepository;

    private final MeetupRepository meetupRepository;

    @Override
    @Transactional
    public void registerMeetUp(MeetupRequestDto meetupRequestDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        Channel channel = channelRepository.findById(meetupRequestDto.getChannelId()).orElseThrow(() -> new BadRequestException("유효하지 않은 채널입니다."));

        Meetup meetup = Meetup.builder()
                .title(meetupRequestDto.getTitle())
                .color(meetupRequestDto.getColor())
                .channel(channel)
                .manager(user)
                .build();

        meetupRepository.save(meetup);

    }

    @Override
    @Transactional
    public void updateMeetup(MeetupRequestDto meetupRequestDto, String userId, Long meetupId) {

        Channel channel = channelRepository.findById(meetupRequestDto.getChannelId()).orElseThrow(() -> new BadRequestException("유효하지 않은 채널입니다."));
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));

        meetup.setColor(meetupRequestDto.getColor());
        meetup.setTitle(meetupRequestDto.getTitle());
        meetup.setChannel(channel);

    }

    @Override
    @Transactional
    public void deleteMeetup(Long meetupId) {
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        meetup.setIsDelete(true);
    }

    @Override
    public List<MeetupResponseDto> getResponseDtos(String userId) {
        User mangerUser = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("유효하지 않은 사용자입니다."));
        List<Meetup> meetups = meetupRepository.findByManager(mangerUser);
        List<MeetupResponseDto> meetupResponseDtos = new ArrayList<>();
        for (Meetup meetup : meetups) {
            if (!meetup.isDelete())
                meetupResponseDtos.add(MeetupResponseDto.of(meetup));
        }
        return meetupResponseDtos;
    }

    @Override
    public List<CalendarResponseDto> getCalendarList(List<ChannelUser> channelUserList) {
        List<Channel> channelList = new ArrayList<>();
        for (ChannelUser channelUser : channelUserList) {
            channelList.add(channelUser.getChannel());
        }

        List<Meetup> meetupList = meetupRepository.findByChannelIn(channelList);
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        for (Meetup meetup : meetupList) {
            if (!meetup.isDelete())
                calendarResponseDtoList.add(CalendarResponseDto.of(meetup.getManager()));
        }

        return calendarResponseDtoList;
    }

    @Override
    public Channel getMeetupChannelById(Long meetupId) {

        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));

        return meetup.getChannel();
    }
}
