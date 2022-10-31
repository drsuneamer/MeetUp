package com.meetup.backend.controller;

import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.meeting.MeetingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * created by myeongseok on 2022/10/23
 * updated by myeongseok on 2022/10/31
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingController {
    private final MeetingService meetingService;
    private final AuthService authService;

    @PostMapping
    @ApiOperation(value ="meeting 일정 등록(나 -> 컨설턴트)")
    public ResponseEntity<?> applyMeeting(@RequestBody @Valid MeetingRequestDto meetingRequestDto) {
        log.info("meetingRequestDto = {}", meetingRequestDto);
        String userId = authService.getMyInfoSecret().getId();
        Long meetingId = meetingService.createMeeting(userId, meetingRequestDto);
        return ResponseEntity.status(CREATED).body(meetingId);
    }

}
