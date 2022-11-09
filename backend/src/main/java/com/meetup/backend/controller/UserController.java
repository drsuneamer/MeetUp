package com.meetup.backend.controller;

import com.meetup.backend.dto.user.LoginRequestDto;
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
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto requestDto) {
        log.info("login");
        return ResponseEntity.status(OK).body(userService.login(requestDto));
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
    public ResponseEntity<?> getMyWebex() {
        return ResponseEntity.status(OK).body(userService.getWebexUrl(authService.getMyInfoSecret().getId()));
    }

    @GetMapping("/webex/{userId}")
    public ResponseEntity<?> getWebex(@PathVariable("userId") String userId) {
        return ResponseEntity.status(OK).body(userService.getWebexUrl(userId));
    }

    @GetMapping("/nickname/{userId}")
    public ResponseEntity<?> getUserNickname(@PathVariable("userId") String userId) {
        return ResponseEntity.status(OK).body(userService.getNickname(userId, authService.getMMSessionToken(authService.getMyInfoSecret().getId())));
    }
}
