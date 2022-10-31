package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.jwt.JwtTokenProvider;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.schedule.ScheduleRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * created by seongmin on 2022/10/31
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
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void before() {
        User con1 = userRepository.save(User.builder()
                .id("consultant")
                .nickname("홍사범")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.Consultant)
                .build());

        User user1 = userRepository.save(User.builder()
                .id("user1")
                .nickname("기영이")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.Student)
                .build());
        User user2 = userRepository.save(User.builder()
                .id("user2")
                .nickname("기철이")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.Student)
                .build());


        System.out.println("===============================================");
        Team team = teamRepository.save(Team.builder()
                .id("team1")
                .name("1team")
                .displayName("1팀")
                .type(TeamType.Open)
                .build());

        teamUserRepository.save(new TeamUser(team, con1));
        teamUserRepository.save(new TeamUser(team, user1));
        teamUserRepository.save(new TeamUser(team, user2));

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


    }

    @Test
    void getScheduleResponseDtoById() {

    }

    @Test
    void getScheduleResponseDtoByUserAndDate() {
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
    void updateSchedule() {
    }

    @Test
    void deleteSchedule() {
    }
}