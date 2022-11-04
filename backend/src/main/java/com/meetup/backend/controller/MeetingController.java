package com.meetup.backend.controller;

import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingChannelDto;
import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.meeting.MeetingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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

    private final ChannelUserService channelUserService;
    @GetMapping("/{meetingID}")
    @ApiOperation(value = "알림을 보낼 채널 선택")
    public ResponseEntity<?> getMeetingDetail(@PathVariable("meetingId") Long meetingId){
        log.info("meetingId = {}", meetingId);
        String userId = authService.getMyInfoSecret().getId();
        MeetingResponseDto meetingResponseDto = meetingService.getMeetingResponseDtoById(userId,meetingId);
        return ResponseEntity.status(OK).body(meetingResponseDto);
    }
    @PostMapping
    @ApiOperation(value = "meeting 일정 등록(나 -> 컨설턴트)")
    public ResponseEntity<?> applyMeeting(@RequestBody @Valid MeetingRequestDto meetingRequestDto) {
        log.info("meetingRequestDto = {}", meetingRequestDto);
        String userId = authService.getMyInfoSecret().getId();
        Long meetingId = meetingService.createMeeting(userId, meetingRequestDto);
        return ResponseEntity.status(CREATED).body(meetingId);
    }


    @GetMapping("/channel/{managerId}")
    @ApiOperation(value = "알림을 보낼 채널 선택")
    public ResponseEntity<?> getAlertChannelList(@PathVariable("managerId") String managerId) {
        String userId = authService.getMyInfoSecret().getId();
        List<MeetingChannelDto> meetingChannelDtoList = channelUserService.getMeetingChannelByUsers(userId, managerId);
        return ResponseEntity.status(OK).body(meetingChannelDtoList);
    }

}
