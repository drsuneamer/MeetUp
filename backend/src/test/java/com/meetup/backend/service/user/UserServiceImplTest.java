package com.meetup.backend.service.user;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${mattermost.id}")
    private String id;

    @Value("${mattermost.password}")
    private String password;

    private String mmId;

    @Test
    @DisplayName("로그인")
    void login() {
        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();
        assertThat(loginResponse).isNotNull();
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();

        String mmSessionToken = redisUtil.getData(mmId);
        log.info("mmSessionToken = {}", mmSessionToken);
        userService.logout(mmSessionToken);
    }

}