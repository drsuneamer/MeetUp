package com.meetup.backend.service.team;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class TeamUserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamUserService teamUserService;

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

    @Value("${mattermost.id}")
    private String id;

    @Value("${mattermost.password}")
    private String password;

    private String mmId;

    @AfterEach
    void After() {
        channelUserRepository.deleteAll();
        channelRepository.deleteAll();
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자가 포함된 팀 목록을 반환")
    void getTeamByUser() {

        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();

        assertThat(teamUserService.getTeamByUser(mmId).size()).isNotSameAs(0);
        log.info("team size = " + teamRepository.findAll().size());

    }
}