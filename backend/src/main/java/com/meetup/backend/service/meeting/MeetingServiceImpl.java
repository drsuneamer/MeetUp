package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.meeting.MeetingRequestDto;
import com.meetup.backend.dto.meeting.MeetingResponseDto;
import com.meetup.backend.dto.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.entity.meeting.Meeting;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.UnAuthorizedException;
import com.meetup.backend.repository.meeting.MeetingRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.util.converter.StringToLocalDateTime;
import jakarta.ws.rs.BadRequestException;
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

    private final MeetingRepository meetingRepository;

    private final MeetupRepository meetupRepository;

    private final UserRepository userRepository;

    @Override
    public MeetingResponseDto getMeetingResponseDtoById(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).get();
        return MeetingResponseDto.of(meeting);
    }

    @Override // 날짜별로 어케 갖고오지 시벌
    public List<MeetingResponseDto> getMeetingResponseDtoByDate(String startDate, String UserId) {
        return null;
    }

    @Override
    @Transactional
    public void createMeeting(String userId, MeetingRequestDto meetingRequestDto) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("유효하지 않은 사용자입니다."));
        if (!userId.equals(meetingRequestDto.getApplicantId())) {
            throw new UnAuthorizedException("권한이 없습니다.");
        }
        User manageUser = userRepository.findById(meetingRequestDto.getManagerId()).get();
        Meetup meetup = meetupRepository.findById(meetingRequestDto.getMeetupId()).get();
        Meeting meeting = Meeting.builder()
                .start(StringToLocalDateTime.strToLDT(meetingRequestDto.getStart()))
                .end(StringToLocalDateTime.strToLDT(meetingRequestDto.getEnd()))
                .title(meetingRequestDto.getTitle())
                .content(meetingRequestDto.getContent())
                .manager(manageUser)
                .applicant(loginUser)
                .meetup(meetup)
                .build();

        meetingRepository.save(meeting);
    }

    @Override
    public void updateMeeting(String userId, MeetingUpdateRequestDto meetingUpdateRequestDto) {

    }

    @Override
    public void deleteMeeting(String userId, Long meetingId) {

    }


}
