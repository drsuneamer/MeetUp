package com.meetup.backend.service.team;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class TeamUserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamUserService teamUserService;

    @Value("${mattermost.id}")
    private String id;

    @Value("${mattermost.password}")
    private String password;

    private String mmId;

    @Transactional
    @Test
    @DisplayName("사용자가 포함된 팀 목록을 반환")
    void getTeamByUser() {

        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();

        assertNotSame(teamUserService.getTeamByUser(mmId), 0);

    }
}