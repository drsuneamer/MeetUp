package com.meetup.backend.controller;

import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.meeting.ScheduleService;
import com.meetup.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * created by myeongseok on 2022/10/23
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    private final UserService userService;

    private final AuthService authService;

    // 스케쥴id로 단일 스케쥴 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getSchedule(@PathVariable("scheduleId") Long scheduleId) {
        log.info("scheduleId = {}", scheduleId);
        String userId = authService.getMyInfoSecret().getId();
        ScheduleResponseDto scheduleResponseDto = scheduleService.getScheduleResponseDtoById(userId, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseDto);

    }

    // 스케쥴 id 해당되는 스케쥴 수정
    @PatchMapping
    public ResponseEntity<?> updateSchedule(@RequestBody @Valid ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        log.info("scheduleUpdateRequestDto : {}", scheduleUpdateRequestDto);
        String userId = authService.getMyInfoSecret().getId();
        scheduleService.updateSchedule(userId, scheduleUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}