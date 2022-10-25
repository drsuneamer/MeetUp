package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.meetup.MeetupRequestDto;

/**
 * created by seungyong on 2022/10/24
 */
public interface MeetupService {

    public void registerMeetUp(MeetupRequestDto meetupRequestDto, String userId);

}
