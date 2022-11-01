package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.schedule.MeetingRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.util.converter.StringToLocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by myeongseok on 2022/10/30
 * updated by seongmin on 2022/11/01
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetingServiceImpl implements MeetingService {

    private final ScheduleRepository scheduleRepository;

    private final ChannelRepository channelRepository;

    private final ChannelUserRepository channelUserRepository;
    private final MeetingRepository meetingRepository;

    private final MeetupRepository meetupRepository;

    private final UserRepository userRepository;

    private final AuthService authService;


    @Override
    public MeetingResponseDto getMeetingResponseDtoById(String userId, Long meetingId) {
        return null;
    }

    @Override
    public List<MeetingResponseDto> getMeetingResponseDtoByUserAndDate(String loginUserId, String date) {
        return null;
    }

    @Override
    public List<MeetingResponseDto> getMeetingResponseDtoByUserAndDate(String loginUserId, String getUserId, Long meetupId, String date) {
        return null;
    }

    @Override
    @Transactional
    public Long createMeeting(String userId, MeetingRequestDto meetingRequestDto) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        LocalDateTime start = StringToLocalDateTime.strToLDT(meetingRequestDto.getStart());
        LocalDateTime end = StringToLocalDateTime.strToLDT(meetingRequestDto.getEnd());
        String title = meetingRequestDto.getTitle();
        String content = meetingRequestDto.getContent();
        Meetup meetup = meetupRepository.findById(meetingRequestDto.getMeetupId()).orElseThrow(() -> new ApiException(ExceptionEnum.MEETUP_NOT_FOUND));
        Channel channel = channelRepository.findById(meetup.getChannel().getId()).orElseThrow(() -> new ApiException(ExceptionEnum.CHANNEL_NOT_FOUND));

        if (channelUserRepository.existsByChannelAndUser(channel, loginUser))
            throw new ApiException(ExceptionEnum.ACCESS_DENIED);

        Meeting meeting = Meeting.builder()
                .title(title)
                .content(content)
                .start(start)
                .end(end)
                .meetup(meetup)
                .user(loginUser)
                .build();
        MattermostClient client = Client.getClient();
        client.setAccessToken(authService.getMMSessionToken(userId));
        String startTime = meetingRequestDto.getStart().substring(5,16);
        String endTime = meetingRequestDto.getStart().substring(11, 16);
        String message = "### " + meetingRequestDto.getTitle() + " \n ###### :bookmark: " + meetingRequestDto.getContent() + " \n ###### :date: " + startTime + " ~ " + endTime + "\n------";
        client.createPost(new Post(channel.getId(), message));
        return meetingRepository.save(meeting).getId();
    }

    @Override
    public void updateMeeting(String userId, MeetingUpdateRequestDto meetingUpdateRequestDto) {

    }

    @Override
    public void deleteMeeting(String userId, Long meetingId) {

    }


}
