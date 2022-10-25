package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.repository.schedule.MeetingRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetingServiceImpl implements MeetingService {

    private final ScheduleRepository scheduleRepository;

    private final MeetingRepository meetingRepository;

    private final MeetupRepository meetupRepository;

    private final UserRepository userRepository;

    @Override
    public MeetingResponseDto getMeetingResponseDtoById(Long meetingId) {
        return null;
    }

    @Override // 날짜별로 어케 갖고오지
    public List<MeetingResponseDto> getMeetingResponseDtoByDate(String startDate, String UserId) {
        return null;
    }

    @Override
    @Transactional
    public void createMeeting(String userId, MeetingRequestDto meetingRequestDto) {
    }

    @Override
    public void updateMeeting(String userId, MeetingUpdateRequestDto meetingUpdateRequestDto) {

    }

    @Override
    public void deleteMeeting(String userId, Long meetingId) {

    }


}
