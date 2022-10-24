package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.meeting.MeetingRequestDto;
import com.meetup.backend.dto.meeting.MeetingResponseDto;
import com.meetup.backend.dto.meeting.MeetingUpdateRequestDto;

import java.util.List;

/**
 * created by seongmin on 2022/10/23
 */
public interface MeetingService {

    public MeetingResponseDto getMeetingResponseDtoById(Long meetingId);

    public List<MeetingResponseDto> getMeetingResponseDtoByDate(String startDate, String UserId);

    public void createMeeting(String userId, MeetingRequestDto meetingRequestDto);

    public void updateMeeting(String userId, MeetingUpdateRequestDto meetingUpdateRequestDto);

    public void deleteMeeting(String userId, Long meetingId);
}
