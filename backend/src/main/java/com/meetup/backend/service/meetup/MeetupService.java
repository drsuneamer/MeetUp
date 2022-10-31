package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.meetup.Meetup;

import java.util.List;

/**
 * created by seungyong on 2022/10/24
 */
public interface MeetupService {

    void registerMeetUp(MeetupRequestDto meetupRequestDto, String userId);

    List<Meetup> getMeetupList(String userId);

    List<Meetup> getCalendarList(List<ChannelUser> channelUserList);

}
