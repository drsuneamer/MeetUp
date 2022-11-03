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

    private final AuthService authService;

    @Override
    @Transactional
    public void registerMeetUp(MeetupRequestDto meetupRequestDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        if (user.getRole().getCode().equals("S") || user.getRole().getCode().equals("A"))
            throw new ApiException(ExceptionEnum.MEETUP_ACCESS_DENIED);
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
    public void updateMeetup(MeetupUpdateRequestDto meetupUpdateRequestDto, String userId, Long meetupId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        if (user.getRole().getCode().equals("S") || user.getRole().getCode().equals("A"))
            throw new ApiException(ExceptionEnum.MEETUP_ACCESS_DENIED);
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        if (meetup.getManager().getId().equals(user.getId()))
            meetup.changeMeetup(meetupUpdateRequestDto.getTitle(), meetupUpdateRequestDto.getColor());
        else
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);

    }

    @Override
    @Transactional
    public void deleteMeetup(Long meetupId, String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        if (user.getRole().getCode().equals("S") || user.getRole().getCode().equals("A"))
            throw new ApiException(ExceptionEnum.MEETUP_ACCESS_DENIED);
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        if (meetup.getManager().getId().equals(user.getId()))
            meetup.deleteMeetup(true);
        else
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);
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
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        List<User> calendarUserList = new ArrayList<>();
        for (Meetup meetup : meetupList) {
            if (meetup.getManager().getId().equals(userId))
                continue;

            if (meetup.isDelete())
                continue;

            if (!calendarUserList.contains(meetup.getManager())) {
                User manager = meetup.getManager();
                if (manager.getNickname() == null) {
                    Response userResponse = client.getUser(manager.getId()).getRawResponse();
                    JSONObject jsonObject = JsonConverter.toJson((BufferedInputStream) userResponse.getEntity());
                    String nickname = (String) jsonObject.get("nickname");
                    manager.setNickname(nickname);
                }
                calendarUserList.add(meetup.getManager());
            }
        }

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
