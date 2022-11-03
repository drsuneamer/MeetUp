package com.meetup.backend.service.user;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

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

    private String mmSessionToken;

    @BeforeEach
    void Before() {
        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();
        mmSessionToken = redisUtil.getData(mmId);
    }

    @AfterEach
    void After() {
        channelUserRepository.deleteAll();
        channelRepository.deleteAll();
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    @DisplayName("로그인")
    void login() {
        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();
        assertThat(loginResponse).isNotNull();

        assertThat(teamRepository.findAll().size()).isNotSameAs(0);

        assertThat(teamUserRepository.findAll().size()).isNotSameAs(0);

        assertThat(channelRepository.findAll().size()).isNotSameAs(0);

        assertThat(channelUserRepository.findAll().size()).isNotSameAs(0);

        assertThat(userRepository.findAll().size()).isNotSameAs(0);
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {

        String mmSessionToken = redisUtil.getData(mmId);
        log.info("mmSessionToken = {}", mmSessionToken);
        userService.logout(mmSessionToken);
    }

    @Test
    @DisplayName("웹엑스 주소 바꾸기 & 가져오기")
    void changeAndGetWebexUrl() {
        String webexUrl = "웹엑스 주소";
        userService.changeWebexUrl(mmId, webexUrl);
        String webex = userService.getWebexUrl(mmId).getWebexUrl();
        assertThat(webex).isEqualTo(webexUrl);
    }

    @Test
    @DisplayName("닉네임 불러오기")
    void getNickname() {

        assertThat(userService.getNickname(mmId)).isNotNull();

    }

}