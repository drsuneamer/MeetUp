package com.meetup.backend.controller;

import com.meetup.backend.dto.admin.ChangeRoleDto;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.service.admin.AdminService;
import com.meetup.backend.service.auth.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import java.util.List;

import static com.meetup.backend.exception.ExceptionEnum.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * created by seongmin on 2022/10/31
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    @Value("${admin.key}")
    private String key;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpDto signUpDto) {
        if (!signUpDto.getKey().equals(key)) {
            throw new ApiException(KEY_NOT_MATCHING);
        }
        adminService.signUp(signUpDto.getId(), signUpDto.getPassword());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.status(OK).body(adminService.login(loginDto.getId(), loginDto.getPassword()));
    }

    @PostMapping("/role")
    public ResponseEntity<?> changeRole(@RequestBody List<ChangeRoleDto> changeRoleDtoList) {
        adminService.changeRole(authService.getMyInfoSecret().getId(), changeRoleDtoList);
        return ResponseEntity.status(CREATED).build();
    }

    @Data
    private static class SignUpDto {
        @NotEmpty(message = "아이디는 비어있을 수 없습니다.")
        String id;
        @NotEmpty(message = "비밀번호는 비어있을 수 없습니다.")
        String password;
        @NotEmpty(message = "key는 비어있을 수 없습니다.")
        String key;
    }

    @Data
    private static class LoginDto {
        @NotEmpty(message = "아이디는 비어있을 수 없습니다.")
        String id;
        @NotEmpty(message = "비밀번호는 비어있을 수 없습니다.")
        String password;
    }
}
