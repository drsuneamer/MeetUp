package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.meetup.MeetupResponseDto;
import com.meetup.backend.entity.channel.ChannelUser;

import java.util.List;

/**
 * created by seungyong on 2022/10/24
 */
public interface MeetupService {

    void registerMeetUp(MeetupRequestDto meetupRequestDto, String userId);

    List<MeetupResponseDto> getResponseDtos(String userId);

    List<MeetupResponseDto> getCalendarList(List<ChannelUser> channelUserList);

}
