package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.party.PartyUser;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.party.PartyRepository;
import com.meetup.backend.repository.schedule.MeetingRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.converter.LocalDateUtil;
import com.meetup.backend.util.exception.MattermostEx;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by myeongseok on 2022/10/30
 * updated by seongmin on 2022/11/14
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetingServiceImpl implements MeetingService {

    private final ChannelRepository channelRepository;

    private final ChannelUserRepository channelUserRepository;

    private final MeetingRepository meetingRepository;

    private final MeetupRepository meetupRepository;

    private final UserRepository userRepository;

    private final PartyRepository partyRepository;

    private final AuthService authService;

    private final ScheduleService scheduleService;

    // 미팅 상세정보 반환
    @Override
    public MeetingResponseDto getMeetingDetail(String userId, Long meetingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new ApiException(MEETING_NOT_FOUND));
        Meetup meetup = meeting.getMeetup(); // 해당 미팅의 밋업
        // 해당 미팅이 그룹에 속해 있는가
        boolean chkGroupIn = false;
        if (meeting.getParty() != null) {
            // 현재 로그인 유저가 미팅의 그룹에 속해있는가?
            List<PartyUser> partyUsers = meeting.getParty().getPartyUsers();
            for (PartyUser partyUser : partyUsers) {
                if (partyUser.getUser().getId().equals(userId)) {
                    chkGroupIn = true;
                    break;
                }
            }
        }
        // 현재 로그인 유저가 채널에 속해있지 않거나, 미팅 관리자가 아니거나,그룹에 속해있지 않다면 접근 불가
        if (!(meeting.getUser().getId().equals(user.getId()) || meeting.getMeetup().getManager().equals(user) || chkGroupIn)) {
            throw new ApiException(ACCESS_DENIED);
        }
        // 만약 해당 미팅이 그룹 소속 되어 있는 미팅이라면
        if (meeting.getParty() != null) {
            Party party = meeting.getParty();
            return MeetingResponseDto.of(meeting, meetup, user, meetup.getManager(), party);
        } else {
            return MeetingResponseDto.of(meeting, meetup, user, meetup.getManager(), null);
        }
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
        LocalDateTime start = LocalDateUtil.strToLDT(meetingRequestDto.getStart());
        LocalDateTime end = LocalDateUtil.strToLDT(meetingRequestDto.getEnd());
        // 시작 시간과 종료 시간의 차이 검사 (30분 이상만 가능)
        scheduleService.diffDurationCheck(start, end);

        String title = meetingRequestDto.getTitle();
        String content = meetingRequestDto.getContent();
        Meetup meetup = meetupRepository.findById(meetingRequestDto.getMeetupId()).orElseThrow(() -> new ApiException(MEETUP_NOT_FOUND));
        Channel channel = channelRepository.findById(meetup.getChannel().getId()).orElseThrow(() -> new ApiException(CHANNEL_NOT_FOUND));

        // 로그인 유저가 해당 밋업에 소속 되어있는지 확인
        if (!channelUserRepository.existsByChannelAndUser(channel, loginUser)) {
            log.error("채널에 속해있지 않음");
            throw new ApiException(ACCESS_DENIED);

        }
        AllScheduleResponseDto userAllScheduleResponseDto = scheduleService.getSchedule(userId, userId, meetingRequestDto.getStart(), 1, scheduleService.getScheduleListWithLock(meetingRequestDto.getStart(), userId, userId, 1));
        AllScheduleResponseDto managerAllScheduleResponseDto = scheduleService.getSchedule(meetup.getManager().getId(), meetup.getManager().getId(), meetingRequestDto.getStart(), 1, scheduleService.getScheduleListWithLock(meetingRequestDto.getStart(), meetup.getManager().getId(), meetup.getManager().getId(), 1));
        // 일정 중복 확인
        if (!userAllScheduleResponseDto.isPossibleRegister(start, end) || !managerAllScheduleResponseDto.isPossibleRegister(start, end)) {
            log.error("스케줄 중복");
            throw new ApiException(DUPLICATE_INSERT_DATETIME);

        }
        // 미팅의 그룹 설정
        Meeting meeting = Meeting.builder().title(title).content(content).start(start).end(end).meetup(meetup).user(loginUser).open(meetingRequestDto.isOpen()).build();
        if (meetingRequestDto.getPartyId() != null) {
            Party party = partyRepository.findById(meetingRequestDto.getPartyId()).orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));
            meeting.setParty(party);
        }

        String startTime = meetingRequestDto.getStart().substring(5, 16);
        startTime = startTime.replaceAll(" ", " (" + LocalDateUtil.getDay(start) + ") ");
        String endTime = meetingRequestDto.getEnd().substring(11, 16);
        String message = "### [:meetup:](meet-up.co.kr) " + meetingRequestDto.getTitle() + " \n ###### :bookmark: " + meetingRequestDto.getContent() + " \n ###### :date: " + startTime + " ~ " + endTime + "\n------";

        mmNotice(meetingRequestDto.isOpen(), loginUser, meetup.getManager().getId(), channel, message);

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
        LocalDateTime start = LocalDateUtil.strToLDT(meetingUpdateRequestDto.getStart());
        LocalDateTime end = LocalDateUtil.strToLDT(meetingUpdateRequestDto.getEnd());
        // 시작 시간과 종료 시간의 차이 검사 (30분 이상만 가능)
        scheduleService.diffDurationCheck(start, end);

        String date = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        AllScheduleResponseDto userAllScheduleResponseDto = scheduleService.getSchedule(userId, userId, date, 1, scheduleService.getScheduleListWithLock(date, userId, userId, 1));
        AllScheduleResponseDto managerAllScheduleResponseDto = scheduleService.getSchedule(userId, managerUser.getId(), date, 1, scheduleService.getScheduleListWithLock(date, userId, managerUser.getId(), 1));

        if (!userAllScheduleResponseDto.isPossibleRegister(start, end, meetingUpdateRequestDto.getId()) || !managerAllScheduleResponseDto.isPossibleRegister(start, end, meetingUpdateRequestDto.getId()))
            throw new ApiException(ExceptionEnum.DUPLICATE_UPDATE_DATETIME);

        Meetup meetup = meetupRepository.findById(meeting.getMeetup().getId()).orElseThrow(() -> new ApiException(MEETUP_NOT_FOUND));
        if (meetup.isDelete()) {
            throw new ApiException(MEETUP_DELETED);
        }

        Channel channel = channelRepository.findById(meetup.getChannel().getId()).orElseThrow(() -> new ApiException(CHANNEL_NOT_FOUND));

        String startTime = meetingUpdateRequestDto.getStart().substring(5, 16);
        String endTime = meetingUpdateRequestDto.getEnd().substring(11, 16);

        startTime = startTime.replaceAll(" ", " (" + LocalDateUtil.getDay(start) + ") ");
        String message = "##### :star2: 미팅 신청이 수정되었습니다. :star2: \n" + "#### 수정 전 \n" + "### [:meetup:](meet-up.co.kr) " + meeting.getTitle() + " \n ###### :bookmark: " + (meeting.getContent() == null ? "" : meeting.getContent()) + " \n ###### :date: " + meeting.getStart().toString().substring(5, 16).replaceAll("T", " ") + " ~ " + meeting.getEnd().toString().substring(11, 16) + "\n------ \n" + "#### 수정 후 \n" + "### [:meetup:](meet-up.co.kr) " + meetingUpdateRequestDto.getTitle() + " \n ###### :bookmark: " + (meetingUpdateRequestDto.getContent() == null ? "" : meetingUpdateRequestDto.getContent()) + " \n ###### :date: " + startTime + " ~ " + endTime + "\n------";

        mmNotice(meetingUpdateRequestDto.isOpen(), user, meetup.getManager().getId(), channel, message);

        meeting.update(meetingUpdateRequestDto, meetup);
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

        Channel channel = channelRepository.findById(meeting.getMeetup().getChannel().getId()).orElseThrow(() -> new ApiException(CHANNEL_NOT_FOUND));


        MattermostClient client = Client.getClient();

        client.setAccessToken(authService.getMMSessionToken(userId));
        String startTime = meeting.getStart().toString().substring(5, 16).replaceAll("T", " ").replaceAll("-", "일").replaceAll(" ", "일 ");

        startTime = startTime.replaceAll(" ", " (" + LocalDateUtil.getDay(meeting.getStart()) + ") ");
        String message = "### :boom: 미팅 취소 알림 :boom: \n" + "##### " + startTime + " 미팅이 취소되었습니다.\n" + "#### [:meetup:](meet-up.co.kr) " + meeting.getTitle() + "\n------";

        if (meeting.getStart().compareTo(LocalDateTime.now()) > 0) {
            if (user == meeting.getMeetup().getManager()) {
                mmNotice(meeting.isOpen(), user, meeting.getUser().getId(), channel, message);
            } else {
                mmNotice(meeting.isOpen(), user, meeting.getMeetup().getManager().getId(), channel, message);

            }
        }
        meetingRepository.delete(meeting);
    }

    private void mmNotice(boolean isOpen, User loginUser, String managerId, Channel channel, String message) {
        MattermostClient client = Client.getClient();

        client.setAccessToken(authService.getMMSessionToken(loginUser.getId()));

        if (isOpen) {
            int status = client.createPost(new Post(channel.getId(), message)).getRawResponse().getStatus();
            if (status == 201 || status == 200) {
                log.info("mattermost 미팅 신청 알림 보내기 성공 status = {}", status);
                return;
            }
            MattermostEx.apiException(status);
        } else {
            Response directChannelResponse = client.createDirectChannel(loginUser.getId(), managerId).getRawResponse();
            int status = directChannelResponse.getStatus();
            MattermostEx.apiException(status);
            JSONObject resObj;
            try {
                resObj = JsonConverter.toJson((BufferedInputStream) directChannelResponse.getEntity());
            } catch (ClassCastException e) {
                log.error(e.getMessage());
                log.info("directChannelResponse.getEntity() = {}", directChannelResponse.getEntity());
                throw new ApiException(MATTERMOST_EXCEPTION);
            }
            String id = resObj.getString("id");

            Response dmResponse = client.createPost(new Post(id, message)).getRawResponse();
            if (dmResponse.getStatus() == 201) {
                log.info("mattermost 미팅 신청 알림(DM) 보내기 성공 status = {}", dmResponse.getStatus());
                return;
            }
            MattermostEx.apiException(dmResponse.getStatus());
        }
    }
}
