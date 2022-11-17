package com.meetup.backend.service.team;

import com.meetup.backend.dto.team.TeamActivateRequestDto;
import com.meetup.backend.dto.team.TeamActivateResponseDto;
import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class TeamUserServiceImplTest {

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private TeamUserService teamUserService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Autowired
//    private TeamUserRepository teamUserRepository;
//
//    @Autowired
//    private ChannelRepository channelRepository;
//
//    @Autowired
//    private ChannelUserRepository channelUserRepository;
//
//    @Value("${mattermost.id}")
//    private String id;
//
//    @Value("${mattermost.password}")
//    private String password;
//
//    private String mmId;
//
//    @BeforeEach
//    void before() {
//        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
//        mmId = loginResponse.getId();
//    }
//
//    @AfterEach
//    void After() {
//        channelUserRepository.deleteAll();
//        channelRepository.deleteAll();
//        teamUserRepository.deleteAll();
//        teamRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("사용자가 포함된 팀 목록을 반환")
//    void getTeamByUser() {
//        assertThat(teamUserService.getTeamByUser(mmId).size()).isNotSameAs(0);
//    }
//
//    @Test
//    @DisplayName("사용자 별로 팀 비활성화 (밋업 생성시에 목록에 표시안됨) & 팀 활성화목록 가져오기")
//    void activateTeamUser() {
//
//        List<TeamActivateRequestDto> teamActivateRequestDtoList = new ArrayList<>();
//        List<TeamUser> teamUserList = teamUserRepository.findByUser(User.builder().id(mmId).build());
//
//        Random random = new Random();
//        for (TeamUser teamUser : teamUserList) {
//            if (random.nextBoolean())
//                teamActivateRequestDtoList.add(new TeamActivateRequestDto(teamUser.getTeam().getId()));
//        }
//
//        teamUserService.activateTeamUser(mmId, teamActivateRequestDtoList);
//        teamUserService.getActivateTeamByUser(mmId);
//        for (TeamActivateResponseDto teamActivateResponseDto : teamUserService.getActivateTeamByUser(mmId)) {
//            log.info("===== team name = {}, is activate= {} =====", teamActivateResponseDto.getDisplayName(), teamActivateResponseDto.getIsActivate());
//        }
//
//        assertThat(teamUserService.getActivateTeamByUser(mmId).size()).isNotSameAs(0);
//
//    }

}