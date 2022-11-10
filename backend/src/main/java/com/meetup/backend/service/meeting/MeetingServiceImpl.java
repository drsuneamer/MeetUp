package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
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
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.converter.StringToLocalDateTime;
import com.meetup.backend.util.exception.MattermostEx;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by myeongseok on 2022/10/30
 * updated by seongmin on 2022/11/09
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

    // 미팅 상세정보 반환
    @Override
    public MeetingResponseDto getMeetingResponseDtoById(String userId, Long meetingId) {
        // 로그인 유저
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new ApiException(MEETING_NOT_FOUND));
        Meetup meetup = meeting.getMeetup(); // 해당 미팅의 밋업
        Channel channel = meetup.getChannel(); // 해당 밋업의 채널
        // 현재 로그인 유저가 채널에 속해있지 않거나, 미팅 관리자가 아닌경우 접근 불가
        if (!meeting.getUser().getId().equals(user.getId()) && !meeting.getMeetup().getManager().equals(user)) {
            throw new ApiException(ACCESS_DENIED);
        }
        return MeetingResponseDto.of(meeting, meetup, user, meetup.getManager());
    }

    //미팅 정보 등록
    @Override
    @Transactional
    public Long createMeeting(String userId, MeetingRequestDto meetingRequestDto) {
        // 입력받은 시간 포멧 검사
        if (meetingRequestDto.getStart().length() != "yyyy-MM-dd HH:mm:ss".length() || meetingRequestDto.getEnd().length() != "yyyy-MM-dd HH:mm:ss".length()) {
            throw new ApiException(DATE_FORMAT_EX);
        }
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        LocalDateTime start = StringToLocalDateTime.strToLDT(meetingRequestDto.getStart());
        LocalDateTime end = StringToLocalDateTime.strToLDT(meetingRequestDto.getEnd());
        // 시작 시간과 종료 시간의 차이 검사 (30분 이상만 가능)
        Duration duration = Duration.between(start, end);
        if (duration.getSeconds() < 1800)
            throw new ApiException(TOO_SHORT_DURATION);
        String title = meetingRequestDto.getTitle();
        String content = meetingRequestDto.getContent();
        Meetup meetup = meetupRepository.findById(meetingRequestDto.getMeetupId()).orElseThrow(() -> new ApiException(MEETUP_NOT_FOUND));
        Channel channel = channelRepository.findById(meetup.getChannel().getId()).orElseThrow(() -> new ApiException(CHANNEL_NOT_FOUND));

        // 로그인 유저가 해당 밋업에 소속 되어있는지 확인
        if (!channelUserRepository.existsByChannelAndUser(channel, loginUser)) {
            log.error("채널에 속해있지 않음");
            throw new ApiException(ACCESS_DENIED);

        }
        AllScheduleResponseDto userAllScheduleResponseDto = getSchedule(userId, userId, meetingRequestDto.getStart(), 1);

        AllScheduleResponseDto managerAllScheduleResponseDto = getSchedule(meetup.getManager().getId(), meetup.getManager().getId(), meetingRequestDto.getStart(), 1);
        // 일정 중복 확인
        if (!userAllScheduleResponseDto.isPossibleRegister(start, end) || !managerAllScheduleResponseDto.isPossibleRegister(start, end)) {
            log.error("스케줄 중복");
            throw new ApiException(DUPLICATE_INSERT_DATETIME);

        }

        Meeting meeting = Meeting.builder().title(title).content(content).start(start).end(end).meetup(meetup).user(loginUser).open(meetingRequestDto.isOpen()).build();
        MattermostClient client = Client.getClient();

        String mmToken = authService.getMMSessionToken(userId);
        log.info("mmToken = {}", mmToken);
        client.setAccessToken(authService.getMMSessionToken(userId));
        String startTime = meetingRequestDto.getStart().substring(5, 16);
        String endTime = meetingRequestDto.getEnd().substring(11, 16);
        String message = "### " + meetingRequestDto.getTitle() + " \n ###### :bookmark: " + meetingRequestDto.getContent() + " \n ###### :date: " + startTime + " ~ " + endTime + "\n------";
        if (meetingRequestDto.isOpen()) {
            int status = client.createPost(new Post(channel.getId(), message)).getRawResponse().getStatus();
            if (status == 201 || status == 200) {
                log.info("mattermost 미팅 신청 알림 보내기 성공 status = {}", status);
                return meetingRepository.save(meeting).getId();
            }
            MattermostEx.apiException(status);
        } else {
            Response directChannelResponse = client.createDirectChannel(loginUser.getId(), meetup.getManager().getId()).getRawResponse();
            int status = directChannelResponse.getStatus();
            MattermostEx.apiException(status);

            JSONObject resObj = JsonConverter.toJson((BufferedInputStream) directChannelResponse.getEntity());
            String id = resObj.getString("id");

            Response dmResponse = client.createPost(new Post(id, message)).getRawResponse();
            if (dmResponse.getStatus() == 201) {
                log.info("mattermost 미팅 신청 알림(DM) 보내기 성공 status = {}", dmResponse.getStatus());
                return meetingRepository.save(meeting).getId();
            }
            MattermostEx.apiException(dmResponse.getStatus());
        }


        return meetingRepository.save(meeting).getId();
    }

    // 미팅 정보 수정
    @Override
    @Transactional
    public Long updateMeeting(String userId, MeetingUpdateRequestDto meetingUpdateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Meeting meeting = meetingRepository.findById(meetingUpdateRequestDto.getId()).orElseThrow(() -> new ApiException(MEETING_NOT_FOUND));
        User managerUser = userRepository.findById(meeting.getMeetup().getManager().getId()).orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        if (!meeting.getMeetup().getManager().equals(user) && !meeting.getUser().equals(user)) {
            throw new ApiException(ACCESS_DENIED);
        }
        LocalDateTime start = StringToLocalDateTime.strToLDT(meetingUpdateRequestDto.getStart());
        LocalDateTime end = StringToLocalDateTime.strToLDT(meetingUpdateRequestDto.getEnd());
        // 시작 시간과 종료 시간의 차이 검사 (30분 이상만 가능)
        Duration duration = Duration.between(start, end);
        if (duration.getSeconds() < 1800)
            throw new ApiException(TOO_SHORT_DURATION);
        String date = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        AllScheduleResponseDto userAllScheduleResponseDto = getSchedule(userId, userId, date, 1);
        AllScheduleResponseDto managerAllScheduleResponseDto = getSchedule(userId, managerUser.getId(), date, 1);

        if (!userAllScheduleResponseDto.isPossibleRegister(start, end, meetingUpdateRequestDto.getId()) || !managerAllScheduleResponseDto.isPossibleRegister(start, end, meetingUpdateRequestDto.getId()))
            throw new ApiException(ExceptionEnum.DUPLICATE_UPDATE_DATETIME);

        Meetup meetup = meetupRepository.findById(meetingUpdateRequestDto.getMeetupId()).orElseThrow(() -> new ApiException(MEETUP_NOT_FOUND));
        Channel channel = channelRepository.findById(meetup.getChannel().getId()).orElseThrow(() -> new ApiException(CHANNEL_NOT_FOUND));

        //mm 알림
        MattermostClient client = Client.getClient();

        client.setAccessToken(authService.getMMSessionToken(userId));
        String startTime = meetingUpdateRequestDto.getStart().substring(5, 16);
        String endTime = meetingUpdateRequestDto.getEnd().substring(11, 16);

        String message = "##### :star2: 미팅 신청이 수정되었습니다. :star2: \n" + "#### 수정 전 \n" +
                "### " + meeting.getTitle() + " \n ###### :bookmark: " + (meeting.getContent() == null ? "" : meeting.getContent()) + " \n ###### :date: " + meeting.getStart().toString().substring(5, 16).replaceAll("T", " ") + " ~ " + meeting.getEnd().toString().substring(11, 16) + "\n------ \n"
                + "#### 수정 후 \n" + "### " + meetingUpdateRequestDto.getTitle() + " \n ###### :bookmark: " + (meetingUpdateRequestDto.getContent() == null ? "" : meetingUpdateRequestDto.getContent()) + " \n ###### :date: " + startTime + " ~ " + endTime + "\n------";


        if (meetingUpdateRequestDto.isOpen()) {
            int status = client.createPost(new Post(channel.getId(), message)).getRawResponse().getStatus();
            if (status == 201 || status == 200) {
                meeting.update(meetingUpdateRequestDto);
                log.info("mattermost 미팅 신청 알림 보내기 성공 status = {}", status);
                return meeting.getId();
            }
            MattermostEx.apiException(status);
        } else {
            Response directChannelResponse = client.createDirectChannel(userId, meetup.getManager().getId()).getRawResponse();
            int status = directChannelResponse.getStatus();
            MattermostEx.apiException(status);

            JSONObject resObject = JsonConverter.toJson((BufferedInputStream) directChannelResponse.getEntity());
            String id = resObject.getString("id");

            Response dmResponse = client.createPost(new Post(id, message)).getRawResponse();
            if (dmResponse.getStatus() == 201) {
                meeting.update(meetingUpdateRequestDto);
                log.info("mattermost 미팅 신청 알림(DM) 보내기 성공 status = {}", dmResponse.getStatus());
                return meeting.getId();
            }
            MattermostEx.apiException(dmResponse.getStatus());
        }
        return meeting.getId();
    }

    // 미팅 정보 삭제
    @Override
    @Transactional
    public void deleteMeeting(String userId, Long meetingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new ApiException(MEETING_NOT_FOUND));
        // 로그인 유저가 미팅 관리자가 아니거나 신청자가 아니라면 삭제 불가
        if (!meeting.getUser().getId().equals(user.getId()) && !meeting.getMeetup().getManager().equals(user)) {
            throw new ApiException(ACCESS_DENIED);
        }
        meetingRepository.delete(meeting);
    }

    public AllScheduleResponseDto getSchedule(String loginUserId, String targetUserId, String date, int p) {
        User loginUser = userRepository.findById(loginUserId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        List<Meetup> meetups = meetupRepository.findByManager(targetUser);

        boolean flag = false;
        if (loginUserId.equals(targetUserId)) {
            flag = true;
        }
        for (Meetup meetup : meetups) {
            if (channelUserRepository.existsByChannelAndUser(meetup.getChannel(), loginUser)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new ApiException(ACCESS_DENIED_THIS_SCHEDULE);
        }
        LocalDateTime from = StringToLocalDateTime.strToLDT(date);
        from = from.minusDays(p);
        LocalDateTime to = from.plusDays(p);
        List<Schedule> schedules = scheduleRepository.findAllByStartBetweenAndUser(from, to, targetUser);


        // 해당 스케줄 주인의 밋업 리스트
        List<Meetup> meetupList = meetupRepository.findByManager(targetUser);
        List<Meeting> meetingToMe = new ArrayList<>();
        if (meetupList.size() > 0) {
            for (Meetup mu : meetupList) {
                // 스케줄 주인이 신청 받은 미팅(컨,프,코,교 시점)
                meetingToMe.addAll(meetingRepository.findByMeetup(mu));
            }
        }
        return AllScheduleResponseDto.of(schedules, meetingToMe, loginUserId);
    }
}
