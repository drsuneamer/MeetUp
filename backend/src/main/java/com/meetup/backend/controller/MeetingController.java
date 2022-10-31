package com.meetup.backend.controller;

import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * created by myeongseok on 2022/10/23
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingController {
    private final MeetingService meetingService;
    private final AuthService authService;

    @PostMapping("/")
    public ResponseEntity<?> applyMeeting(@RequestBody @Valid com.meetup.backend.dto.schedule.meeting.MeetingRequestDto requestDto) {
        return null;

    }

}
