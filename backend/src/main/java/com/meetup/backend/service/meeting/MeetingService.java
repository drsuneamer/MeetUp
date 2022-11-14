package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;

/**
 * created by seongmin on 2022/10/23
 */
public interface MeetingService {
    MeetingResponseDto getMeetingDetail(String userId, Long meetingId);

    Long createMeeting(String userId, MeetingRequestDto meetingRequestDto);

    Long updateMeeting(String userId, MeetingUpdateRequestDto meetingUpdateRequestDto);

    void deleteMeeting(String userId, Long meetingId);
}
