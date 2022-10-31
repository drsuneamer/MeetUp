package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.meetup.MeetupResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.User;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("유효하지 않은 사용자입니다."));
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
    public List<MeetupResponseDto> getResponseDtos(String userId) {
        User mangerUser = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("유효하지 않은 사용자입니다."));
        List<Meetup> meetups = meetupRepository.findByManager(mangerUser);
        List<MeetupResponseDto> meetupResponseDtos = new ArrayList<>();
        for (Meetup meetup : meetups) {
            meetupResponseDtos.add(MeetupResponseDto.of(meetup));
        }
        return meetupResponseDtos;
    }
}
