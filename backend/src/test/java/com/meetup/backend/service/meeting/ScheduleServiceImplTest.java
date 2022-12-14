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
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.util.converter.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * created by seongmin on 2022/10/31
 * updated by seongmin on 2022/11/01
 */
@SpringBootTest
@Slf4j
class ScheduleServiceImplTest {
//
//    @Autowired
//    private ScheduleService scheduleService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private TeamRepository teamRepository;
//    @Autowired
//    private TeamUserRepository teamUserRepository;
//    @Autowired
//    private ChannelRepository channelRepository;
//    @Autowired
//    private ChannelUserRepository channelUserRepository;
//
//    @Autowired
//    private MeetupRepository meetupRepository;
//
//    @Autowired
//    private ScheduleRepository scheduleRepository;
//
//    @Autowired
//    private MeetingService meetingService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private Meetup consultantMeetup;
//    private Meetup coachMeetup;
//
//    @BeforeEach
//    void before() {
//        User con1 = userRepository.save(User.builder()
//                .id("consultant")
//                .nickname("?????????")
//                .password(passwordEncoder.encode("password123"))
//                .firstLogin(true)
//                .role(RoleType.ROLE_Consultant)
//                .build());
//
//        User user1 = userRepository.save(User.builder()
//                .id("user1")
//                .nickname("?????????")
//                .password(passwordEncoder.encode("password123"))
//                .firstLogin(true)
//                .role(RoleType.ROLE_Student)
//                .build());
//        User user2 = userRepository.save(User.builder()
//                .id("user2")
//                .nickname("?????????")
//                .password(passwordEncoder.encode("password123"))
//                .firstLogin(true)
//                .role(RoleType.ROLE_Student)
//                .build());
//        User coach1 = userRepository.save(User.builder()
//                .id("coach1")
//                .nickname("?????????")
//                .password(passwordEncoder.encode("password123"))
//                .firstLogin(true)
//                .role(RoleType.ROLE_Coach)
//                .build());
//
//
//        Team team = teamRepository.save(Team.builder()
//                .id("team1")
//                .name("1team")
//                .displayName("1???")
//                .type(TeamType.Open)
//                .build());
//
//        teamUserRepository.save(new TeamUser(team, con1));
//        teamUserRepository.save(new TeamUser(team, user1));
//        teamUserRepository.save(new TeamUser(team, user2));
//        teamUserRepository.save(new TeamUser(team, coach1));
//
//        Channel channel1 = channelRepository.save(Channel.builder()
//                .id("channel1")
//                .name("1channel")
//                .displyName("??????1")
//                .team(team)
//                .type(ChannelType.Open)
//                .build());
//        Channel channel2 = channelRepository.save(Channel.builder()
//                .id("channel2")
//                .name("1channe2")
//                .displyName("??????2")
//                .team(team)
//                .type(ChannelType.Open)
//                .build());
//
//        // con1, user1??? channel 1, 2 ?????? ??????
//        // user2??? channel1?????? ??????.
//        channelUserRepository.save(new ChannelUser(con1, channel1));
//        channelUserRepository.save(new ChannelUser(con1, channel2));
//        channelUserRepository.save(new ChannelUser(user1, channel1));
//        channelUserRepository.save(new ChannelUser(user2, channel1));
//        channelUserRepository.save(new ChannelUser(user1, channel2));
//        channelUserRepository.save(new ChannelUser(coach1, channel1));
//        channelUserRepository.save(new ChannelUser(coach1, channel2));
//
//        consultantMeetup = meetupRepository.save(new Meetup("???????????? ?????? ??????", "AAAAA", con1, channel1));
//        coachMeetup = meetupRepository.save(new Meetup("?????? ?????? ??????", "BBBBB", coach1, channel2));
//    }
//
//    @AfterEach
//    void after() {
//        scheduleRepository.deleteAll();
//        meetupRepository.deleteAll();
//        channelUserRepository.deleteAll();
//        channelRepository.deleteAll();
//        teamUserRepository.deleteAll();
//        teamRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    //yyyy-MM-dd hh:mm:ss
//    @Test
//    @DisplayName("??? ????????? ??????")
//    void createSchedule() {
//        User user1 = userRepository.findById("user1").get();
//        Long scheduleId = scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                "2022-10-30 10:00:00", "2022-10-30 10:30:00", "title", "??????", true
//        ));
//        assertThat(scheduleRepository.findById(scheduleId).get().getTitle()).isEqualTo("title");
//
//    }
//
//    @Test()
//    @DisplayName("?????? ????????? ????????? ????????? ?????? ??????")
//    void refuseCreatingSchedule() {
//        User user1 = userRepository.findById("user1").get();
//        // ????????? ??????
//        Long scheduleId = scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                "2022-10-30 08:00:00", "2022-10-30 10:00:00", "title", "??????", true
//        ));
//        // ????????? 1??? ?????? ??????
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(1);
//        assertThat(scheduleRepository.findAll().size()).isEqualTo(1);
//        List<Schedule> all = scheduleRepository.findAll();
//        for (Schedule schedule : all) {
//            log.info("schedule title = {}", schedule.getTitle());
//        }
////         1. ????????? ?????? ?????? ?????? ( ????????? ???????????? ?????? ?????? ???????????? ???????????? ??? _ ?????? ???????????????)
////        Long schedule = scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
////                "2022-10-30 09:30:00", "2022-10-30 10:30:00", "title1", "??????1", true)
////        );
////        log.info("?????? ????????? id= {}", schedule);
//        assertThatThrownBy(() -> {
//            scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                    "2022-10-30 07:30:00", "2022-10-30 09:30:00", "title1", "??????1", true)
//            );
//        }).isInstanceOf(ApiException.class);
//        ;
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(1);
//
//        // 2. ????????? ?????? ?????? ?????? ( ????????? ???????????? ????????? ?????? ???????????? ????????? ??? _ ?????? ???????????????)
//        assertThatThrownBy(() -> {
//            scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                    "2022-10-30 09:30:00", "2022-10-30 11:30:00", "title2", "??????2", true)
//            );
//        }).isInstanceOf(ApiException.class).hasMessageContaining("??????").hasMessageContaining("??????");
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(1);
//
//        // 3. ????????? ?????? ?????? ?????? ( ????????? ???????????? ?????? ???????????? ?????? ?????? _ ?????? ???????????????)
//        assertThatThrownBy(() -> {
//            scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                    "2022-10-30 07:30:00", "2022-10-30 11:30:00", "title2", "??????2", true)
//            );
//        }).isInstanceOf(ApiException.class).hasMessageContaining("??????").hasMessageContaining("??????");
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(1);
//
//        // 4. ????????? ?????? ?????? ?????? ( ????????? ???????????? ?????? ???????????? ?????? ????????? _ ?????? ???????????????)
//        assertThatThrownBy(() -> {
//            scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                    "2022-10-30 09:00:00", "2022-10-30 10:00:00", "title2", "??????2", true)
//            );
//        }).isInstanceOf(ApiException.class).hasMessageContaining("??????").hasMessageContaining("??????");
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("????????? 1?????? ????????? ??????")
//    void getScheduleByUserAndDate() {
//        User user1 = userRepository.findById("user1").get();
//        scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                "2022-10-25 10:00:00", "2022-10-25 10:30:00", "title", "??????", true
//        ));
//        User con1 = userRepository.findById("consultant").get();
//        scheduleService.createSchedule(con1.getId(), new ScheduleRequestDto(
//                "2022-10-26 10:00:00", "2022-10-26 10:30:00", "title2", "con ??????", true
//        ));
//        User coach1 = userRepository.findById("coach1").get();
//
//        // user1??? con1?????? ?????? ??????
//        meetingService.createMeeting(user1.getId(), new MeetingRequestDto("2022-10-28 10:00:00", "2022-10-28 11:00:00", "2??? ????????????", "????????? ??????", consultantMeetup.getId(), null, true));
//        // con1??? coach1?????? ?????? ??????
//        meetingService.createMeeting(con1.getId(), new MeetingRequestDto("2022-10-27 10:00:00", "2022-10-27 11:00:00", "?????? ??????", "????????? ??????", coachMeetup.getId(), null, true));
//
//
//        AllScheduleResponseDto result = scheduleService.getScheduleByUserAndDate(user1.getId(), con1.getId(), "2022-10-23 10:00:00");
//
//        assertThat(result.getScheduleResponseList().size()).isEqualTo(1); // ??? ?????????
//        assertThat(result.getMeetingFromMe().size()).isEqualTo(1); // ?????? ????????? ??????
//        assertThat(result.getMeetingToMe().size()).isEqualTo(1); // ?????? ???????????? ??????
//    }
//
//    @Test
//    @DisplayName("????????? ????????? ??????")
//    void getScheduleDetail() {
//        User user = userRepository.findById("user1").get();
//        Long scheduleId = scheduleService.createSchedule(user.getId(), new ScheduleRequestDto(
//                "2022-10-25 10:00:00", "2022-10-25 10:30:00", "title", "??????", true
//        ));
//        ScheduleResponseDto result = scheduleService.getScheduleDetail(user.getId(), scheduleId);
//        assertThat(result.getTitle()).isEqualTo("title");
//        assertThat(result.getContent()).isEqualTo("??????");
//    }
//
//    @Test
//    @DisplayName("????????? ??????")
//    void updateSchedule() {
//        User user1 = userRepository.findById("user1").get();
//        Long scheduleId = scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                "2022-10-30 18:00:00", "2022-10-30 20:30:00", "title1", "??????1", true
//        ));
//        Long scheduleId2 = scheduleService.createSchedule(user1.getId(), new ScheduleRequestDto(
//                "2022-10-30 08:00:00", "2022-10-30 11:00:00", "title2", "??????2", true
//        ));
//        // ????????? 2??? ?????? ??????
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(2);
//
//        // 1. ????????? ?????? ?????? ?????? ( ????????? ???????????? ?????? ?????? ???????????? ???????????? ??? _ ?????? ???????????????)
//        assertThatThrownBy(() -> {
//            scheduleService.updateSchedule(user1.getId(), new ScheduleUpdateRequestDto(
//                    scheduleId, "2022-10-30 09:30:00", "2022-10-30 10:30:00", "title1", "??????1", true)
//            );
//        }).isInstanceOf(ApiException.class).hasMessageContaining("??????").hasMessageContaining("??????");
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(2);
//
//        // 2. ????????? ?????? ?????? ?????? ( ????????? ???????????? ????????? ?????? ???????????? ????????? ??? _ ?????? ???????????????)
//        assertThatThrownBy(() -> {
//            scheduleService.updateSchedule(user1.getId(), new ScheduleUpdateRequestDto(
//                    scheduleId, "2022-10-30 10:30:00", "2022-10-30 11:30:00", "title2", "??????2", true)
//            );
//        }).isInstanceOf(ApiException.class).hasMessageContaining("??????").hasMessageContaining("??????");
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(2);
//
//        // 3. ????????? ?????? ?????? ?????? ( ????????? ???????????? ?????? ???????????? ?????? ?????? _ ?????? ???????????????)
//        assertThatThrownBy(() -> {
//            scheduleService.updateSchedule(user1.getId(), new ScheduleUpdateRequestDto(
//                    scheduleId, "2022-10-30 09:30:00", "2022-10-30 11:30:00", "title2", "??????2", true)
//            );
//        }).isInstanceOf(ApiException.class).hasMessageContaining("??????").hasMessageContaining("??????");
//        ;
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(2);
//
//        // 4. ????????? ?????? ?????? ?????? ( ????????? ???????????? ?????? ???????????? ?????? ????????? _ ?????? ???????????????)
//        assertThatThrownBy(() -> {
//            scheduleService.updateSchedule(user1.getId(), new ScheduleUpdateRequestDto(
//                    scheduleId, "2022-10-30 10:00:00", "2022-10-30 10:30:00", "title2", "??????2", true)
//            );
//        }).isInstanceOf(ApiException.class).hasMessageContaining("??????").hasMessageContaining("??????");
//        ;
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(2);
//
//        // ??????????????? _ ???????????? ?????? ?????? 2????????????
//        scheduleService.updateSchedule(user1.getId(), new ScheduleUpdateRequestDto(
//                scheduleId, "2022-10-30 15:00:00", "2022-10-30 16:00:00", "modified title", "??????", true
//        ));
//
//        Schedule schedule = scheduleRepository.findById(scheduleId).get();
//        assertThat(schedule.getTitle()).isEqualTo("modified title");
//        assertThat(schedule.getContent()).isEqualTo("??????");
//        assertThat(schedule.getStart()).isEqualTo(StringToLocalDateTime.strToLDT("2022-10-30 15:00:00"));
//
//        assertThat(scheduleService.getScheduleByUserAndDate(user1.getId(), user1.getId(), "2022-10-30 00:00:00").getScheduleResponseList().size()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("????????? ??????")
//    void deleteSchedule() {
//        User user = userRepository.findById("user1").get();
//        Long scheduleId = scheduleService.createSchedule(user.getId(), new ScheduleRequestDto(
//                "2022-10-25 10:00:00", "2022-10-25 10:30:00", "title", "??????", true
//        ));
//        scheduleService.deleteSchedule(user.getId(), scheduleId);
//        assertThat(scheduleRepository.findById(scheduleId).isPresent()).isFalse();
//    }
}