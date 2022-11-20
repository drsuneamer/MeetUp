package com.meetup.backend.controller;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.dto.user.UserWebexInfoDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seongmin on 2022/10/25
 * updated by seongmin on 2022/11/02
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        log.info("login");
        LoginResponseDto loginResponseDto = userService.login(requestDto);
        return ResponseEntity.status(OK).body(loginResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        userService.logout(authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).build();
    }

    @PutMapping("/webex")
    public ResponseEntity<?> setWebex(@RequestBody @Valid UserWebexInfoDto userWebexInfoDto) {
        userService.changeWebexUrl(authService.getMyInfoSecret().getId(), userWebexInfoDto.getWebexUrl());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/webex")
    public ResponseEntity<UserWebexInfoDto> getMyWebex() {
        UserWebexInfoDto userWebexInfoDto = userService.getWebexUrl(authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).body(userWebexInfoDto);
    }

    @GetMapping("/webex/{userId}")
    public ResponseEntity<UserWebexInfoDto> getWebex(@PathVariable("userId") String userId) {
        UserWebexInfoDto userWebexInfoDto = userService.getWebexUrl(userId);
        return ResponseEntity.status(OK).body(userWebexInfoDto);
    }

    @GetMapping("/nickname/{userId}")
    public ResponseEntity<String> getUserNickname(@PathVariable("userId") String userId) {
        String nickName = userService.getNickname(userId, authService.getMMSessionToken(authService.getMyInfoSecret().getId()));
        return ResponseEntity.status(OK).body(nickName);
    }
}
