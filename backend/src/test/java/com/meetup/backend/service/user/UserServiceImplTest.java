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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Transactional
    @Test
    @DisplayName("로그인")
    void login() {
        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();
        assertThat(loginResponse).isNotNull();

        assertNotSame(teamRepository.findAll().size(), 0);

        assertNotSame(teamUserRepository.findAll().size(), 0);

        assertNotSame(channelRepository.findAll().size(), 0);

        assertNotSame(channelUserRepository.findAll().size(), 0);

        assertNotSame(userRepository.findAll().size(), 1);
    }

    @Transactional
    @Test
    @DisplayName("로그아웃")
    void logout() {

        String mmSessionToken = redisUtil.getData(mmId);
        log.info("mmSessionToken = {}", mmSessionToken);
        userService.logout(mmSessionToken);
    }

    @Transactional
    @Test
    @DisplayName("웹엑스 주소 바꾸기")
    void changeWebexUrl() {
        String webexUrl = "웹엑스 주소";
        userService.changeWebexUrl(mmId, webexUrl);
        String webex = userService.getWebexUrl(mmId).getWebexUrl();
        assertEquals(webex, webexUrl);
    }

    @Transactional
    @Test
    @DisplayName("웹엑스 주소 가져오기")
    void getWebexUrl() {
        userService.changeWebexUrl(mmId, "웹엑스 주소");
        String webex = userService.getWebexUrl(mmId).getWebexUrl();
        assertNotNull(webex);
    }

    @Test
    @DisplayName("닉네임 불러오기")
    void getNickname() {

        assertNotNull(userService.getNickname(mmId));

    }

}