package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.util.converter.StringToLocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

/**
 * created by seongmin on 2022/10/31
 * updated by seongmin on 2022/11/01
 */
@SpringBootTest
@Slf4j
class ScheduleServiceImplTest {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamUserRepository teamUserRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelUserRepository channelUserRepository;

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Meetup consultantMeetup;
    private Meetup coachMeetup;

    @BeforeEach
    void before() {
        User con1 = userRepository.save(User.builder()
                .id("consultant")
                .nickname("홍사범")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Consultant)
                .build());

        User user1 = userRepository.save(User.builder()
                .id("user1")
                .nickname("기영이")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Student)
                .build());
        User user2 = userRepository.save(User.builder()
                .id("user2")
                .nickname("기철이")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Student)
                .build());
        User coach1 = userRepository.save(User.builder()
                .id("coach1")
                .nickname("나코치")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Coach)
                .build());


        Team team = teamRepository.save(Team.builder()
                .id("team1")
                .name("1team")
                .displayName("1팀")
                .type(TeamType.Open)
                .build());

        teamUserRepository.save(new TeamUser(team, con1));
        teamUserRepository.save(new TeamUser(team, user1));
        teamUserRepository.save(new TeamUser(team, user2));
        teamUserRepository.save(new TeamUser(team, coach1));

        Channel channel1 = channelRepository.save(Channel.builder()
                .id("channel1")
                .name("1channel")
                .displyName("채널1")
                .team(team)
                .type(ChannelType.Open)
                .build());
        Channel channel2 = channelRepository.save(Channel.builder()
                .id("channel2")
                .name("1channe2")
                .displyName("채널2")
                .team(team)
                .type(ChannelType.Open)
                .build());

        // con1, user1은 channel 1, 2 모두 속함
        // user2는 channel1에만 속함.
        channelUserRepository.save(new ChannelUser(con1, channel1));
        channelUserRepository.save(new ChannelUser(con1, channel2));
        channelUserRepository.save(new ChannelUser(user1, channel1));
        channelUserRepository.save(new ChannelUser(user2, channel1));
        channelUserRepository.save(new ChannelUser(user1, channel2));
        channelUserRepository.save(new ChannelUser(coach1, channel1));
        channelUserRepository.save(new ChannelUser(coach1, channel2));

        consultantMeetup = meetupRepository.save(new Meetup("컨설턴트 미팅 밋업", "AAAAA", con1, channel1));
        coachMeetup = meetupRepository.save(new Meetup("코치 미팅 밋업", "BBBBB", coach1, channel2));
    }

    @AfterEach
    void after() {
        scheduleRepository.deleteAll();
        meetupRepository.deleteAll();
        channelUserRepository.deleteAll();
        channelRepository.deleteAll();
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    //yyyy-MM-dd hh:mm:ss
    @Test
    @DisplayName("내 스케줄 생성")
    void createSchedule() {
        User user1 = userRepository.findById("user1").get();
        Long scheduleId = scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
                "2022-10-30 10:00:00", "2022-10-30 10:30:00", "title", "본문"
        ));
        assertThat(scheduleRepository.findById(scheduleId).get().getTitle()).isEqualTo("title");

    }

    @Test
    @DisplayName("대상의 1주일 스케줄 조회")
    void getScheduleByUserAndDate() {
        User user1 = userRepository.findById("user1").get();
        scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
                "2022-10-25 10:00:00", "2022-10-25 10:30:00", "title", "본문"
        ));
        User con1 = userRepository.findById("consultant").get();
        scheduleService.createSchedule(con1.getId(), new ScheduleRequestDto(
                "2022-10-26 10:00:00", "2022-10-26 10:30:00", "title2", "con 본문"
        ));
        User coach1 = userRepository.findById("coach1").get();

        // user1이 con1에게 미팅 신청
        meetingService.createMeeting(user1.getId(), new MeetingRequestDto("2022-10-28 10:00:00", "2022-10-28 11:00:00", "2팀 미팅신청", "마지막 미팅", consultantMeetup.getId()));
        // con1이 coach1에게 미팅 신청
        meetingService.createMeeting(con1.getId(), new MeetingRequestDto("2022-10-27 10:00:00", "2022-10-27 11:00:00", "컨코 미팅", "한시간 미팅", coachMeetup.getId()));


        AllScheduleResponseDto result = scheduleService.getScheduleByUserAndDate(user1.getId(), con1.getId(), "2022-10-23 10:00:00");

        assertThat(result.getScheduleResponseList().size()).isEqualTo(1); // 내 스케줄
        assertThat(result.getMeetingFromMe().size()).isEqualTo(1); // 내가 신청한 미팅
        assertThat(result.getMeetingToMe().size()).isEqualTo(1); // 내가 신청받은 미팅
    }

    @Test
    @DisplayName("스케줄 디테일 조회")
    void getScheduleDetail() {
        User user = userRepository.findById("user1").get();
        Long scheduleId = scheduleService.createSchedule(user.getId(), new ScheduleRequestDto(
                "2022-10-25 10:00:00", "2022-10-25 10:30:00", "title", "본문"
        ));
        ScheduleResponseDto result = scheduleService.getScheduleResponseDtoById(user.getId(), scheduleId);
        assertThat(result.getTitle()).isEqualTo("title");
        assertThat(result.getContent()).isEqualTo("본문");
    }

    @Test
    @DisplayName("스케줄 수정")
    void updateSchedule() {
        User user = userRepository.findById("user1").get();
        Long scheduleId = scheduleService.createSchedule(user.getId(), new ScheduleRequestDto(
                "2022-10-25 10:00:00", "2022-10-25 10:30:00", "title", "본문"
        ));
        scheduleService.updateSchedule(user.getId(), new ScheduleUpdateRequestDto(
                scheduleId, "2022-10-26 10:00:00", "2022-10-26 10:00:00", "modified title", "본문"
        ));

        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getTitle()).isEqualTo("modified title");
        assertThat(schedule.getContent()).isEqualTo("본문");
        assertThat(schedule.getStart()).isEqualTo(StringToLocalDateTime.strToLDT("2022-10-26 10:00:00"));
    }

    @Test
    @DisplayName("스케줄 삭제")
    void deleteSchedule() {
        User user = userRepository.findById("user1").get();
        Long scheduleId = scheduleService.createSchedule(user.getId(), new ScheduleRequestDto(
                "2022-10-25 10:00:00", "2022-10-25 10:30:00", "title", "본문"
        ));
        scheduleService.deleteSchedule(user.getId(), scheduleId);
        assertThat(scheduleRepository.findById(scheduleId).isPresent()).isFalse();
    }
}