package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.*;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.exception.MattermostEx;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * created by seungyong on 2022/10/24
 * updated by seongmin on 2022/11/10
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetupServiceImpl implements MeetupService {

    private final ChannelRepository channelRepository;

    private final UserRepository userRepository;

    private final MeetupRepository meetupRepository;

    private final AuthService authService;

    @Override
    @Transactional
    public void registerMeetUp(MeetupRequestDto meetupRequestDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        if (user.getRole().getCode().equals("S") || user.getRole().getCode().equals("A"))
            throw new ApiException(ExceptionEnum.MEETUP_ACCESS_DENIED);

        Channel channel = channelRepository.findById(meetupRequestDto.getChannelId()).orElseThrow(() -> new ApiException(ExceptionEnum.CHANNEL_NOT_FOUND));
        if (meetupRepository.existsByManagerAndChannel(user, channel)) {
            Meetup meetup = meetupRepository.findByManagerAndChannel(user, channel).orElseThrow(() -> new ApiException(ExceptionEnum.CHANNEL_NOT_FOUND));
            if (meetup.isDelete()) {
                meetup.reviveMeetup(meetupRequestDto.getTitle(), meetupRequestDto.getColor());
                return;
            } else {
                throw new ApiException(ExceptionEnum.DUPLICATE_MEETUP);
            }
        }

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
    public void updateMeetup(MeetupUpdateRequestDto meetupUpdateRequestDto, String userId, Long meetupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        if (user.getRole().getCode().equals("S") || user.getRole().getCode().equals("A"))
            throw new ApiException(ExceptionEnum.MEETUP_ACCESS_DENIED);

        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        if (!meetup.getManager().getId().equals(user.getId()))
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);

        meetup.changeMeetup(meetupUpdateRequestDto.getTitle(), meetupUpdateRequestDto.getColor());
    }

    @Override
    @Transactional
    public void deleteMeetup(Long meetupId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        if (user.getRole().getCode().equals("S") || user.getRole().getCode().equals("A"))
            throw new ApiException(ExceptionEnum.MEETUP_ACCESS_DENIED);

        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        if (!meetup.getManager().getId().equals(user.getId()))
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);

        meetup.deleteMeetup();
    }

    @Override
    public List<MeetupResponseDto> getResponseDtoList(String userId) {
        User mangerUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        List<Meetup> meetups = meetupRepository.findByManager(mangerUser);
        List<MeetupResponseDto> meetupResponseDtoList = new ArrayList<>();
        for (Meetup meetup : meetups) {
            if (meetup.isDelete())
                continue;
            meetupResponseDtoList.add(MeetupResponseDto.of(meetup));
        }
        return meetupResponseDtoList;
    }

    @Override
    public MeetupUpdateResponseDto getMeetupInfo(Long meetupId) {
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        return MeetupUpdateResponseDto.of(meetup);
    }

    @Override
    public List<CalendarResponseDto> getCalendarList(String userId, List<ChannelUser> channelUserList) {
        List<Channel> channelList = new ArrayList<>();
        for (ChannelUser channelUser : channelUserList) {
            channelList.add(channelUser.getChannel());
        }

        MattermostClient client = Client.getClient();
        client.setAccessToken(authService.getMMSessionToken(userId));

        List<Meetup> meetupList = meetupRepository.findByChannelIn(channelList);
        List<User> calendarUserList = new ArrayList<>();

        for (Meetup meetup : meetupList) {
            if (meetup.getManager().getId().equals(userId))
                continue;

            if (meetup.isDelete())
                continue;

            if (calendarUserList.contains(meetup.getManager()))
                continue;

            calendarUserList.add(meetup.getManager());
        }

        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();

        for (User user : calendarUserList) {
            calendarResponseDtoList.add(CalendarResponseDto.of(user));
        }

        return calendarResponseDtoList;
    }

    @Override
    public Channel getMeetupChannelById(Long meetupId) {
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        return meetup.getChannel();
    }
}
