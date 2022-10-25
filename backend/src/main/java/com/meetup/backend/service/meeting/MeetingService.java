package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;

import java.util.List;

/**
 * created by seongmin on 2022/10/23
 */
public interface MeetingService {

    MeetingResponseDto getMeetingResponseDtoById(Long meetingId);

    List<MeetingResponseDto> getMeetingResponseDtoByDate(String startDate, String UserId);

    void createMeeting(String userId, MeetingRequestDto meetingRequestDto);

    void updateMeeting(String userId, MeetingUpdateRequestDto meetingUpdateRequestDto);

    void deleteMeeting(String userId, Long meetingId);
}
