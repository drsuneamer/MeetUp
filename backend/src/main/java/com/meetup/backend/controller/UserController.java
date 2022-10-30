package com.meetup.backend.controller;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seongmin on 2022/10/25
 * updated by seongmin on 2022/10/30
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto requestDto) {
        return ResponseEntity.status(OK).body(userService.login(requestDto));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        userService.logout(authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).build();
    }
}
