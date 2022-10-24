package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.MeetupRequestDto;
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
    public void registerMeetUp(MeetupRequestDto meetupRequestDto) {
        User user = userRepository.findById("유저아이디").orElseThrow(() -> new BadRequestException("유효하지 않은 사용자입니다."));
        Channel channel = channelRepository.findById(meetupRequestDto.getChannelId()).orElseThrow(() -> new BadRequestException("유효하지 않은 채널입니다."));

        Meetup meetup = Meetup.builder()
                .title(meetupRequestDto.getTitle())
                .color(meetupRequestDto.getColor())
                .channel(channel)
                .manager(user)
                .build();

        meetupRepository.save(meetup);

    }
}
