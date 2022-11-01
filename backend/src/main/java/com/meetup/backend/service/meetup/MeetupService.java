package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.CalendarResponseDto;
import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.meetup.MeetupResponseDto;
import com.meetup.backend.dto.meetup.MeetupUpdateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;

import java.util.List;

/**
 * created by seungyong on 2022/10/24
 * updated by seungyong on 2022/11/01
 */
public interface MeetupService {

    void registerMeetUp(MeetupRequestDto meetupRequestDto, String userId);

    void updateMeetup(MeetupUpdateRequestDto meetupUpdateRequestDto, String userId, Long meetupId);

    void deleteMeetup(Long meetupId, String userId);

    List<MeetupResponseDto> getResponseDtos(String userId);

    List<CalendarResponseDto> getCalendarList(List<ChannelUser> channelUserList);

    Channel getMeetupChannelById(Long meetupId);

}
