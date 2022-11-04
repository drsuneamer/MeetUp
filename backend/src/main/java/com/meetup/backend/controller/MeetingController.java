package com.meetup.backend.controller;

import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingChannelDto;
import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
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
 * updated by myeongseok on 2022/11/04
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingController {
    private final MeetingService meetingService;
    private final AuthService authService;

    private final ChannelUserService channelUserService;

    @GetMapping("/{meetingId}")
    @ApiOperation(value = "알림을 보낼 채널 선택")
    public ResponseEntity<?> getMeetingDetail(@PathVariable("meetingId") Long meetingId) {
        log.info("meetingId = {}", meetingId);
        String userId = authService.getMyInfoSecret().getId();
        MeetingResponseDto meetingResponseDto = meetingService.getMeetingResponseDtoById(userId, meetingId);
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

    // 미팅 id 해당되는 스케쥴 수정
    @PatchMapping
    @ApiOperation(value = "스케쥴 ID에 해당되는 스케쥴 수정")
    public ResponseEntity<?> updateSchedule(@RequestBody @Valid MeetingUpdateRequestDto meetingUpdateRequestDto) {
        log.info("meetingUpdateRequestDto : {}", meetingUpdateRequestDto);
        String userId = authService.getMyInfoSecret().getId();
        Long scheduleId = meetingService.updateMeeting(userId, meetingUpdateRequestDto);

        return ResponseEntity.status(CREATED).body(scheduleId);
    }

    // 미팅 id 해당되는 스케쥴 수정
    @DeleteMapping("/{meetingId}")
    @ApiOperation(value = "스케쥴 ID에 해당되는 스케쥴 삭제")
    public ResponseEntity<?> deleteSchedule(@PathVariable("meetingId") Long meetingId) {
        log.info("meetingId :{}", meetingId);
        String userId = authService.getMyInfoSecret().getId();
        meetingService.deleteMeeting(userId, meetingId);

        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/channel/{managerId}")
    @ApiOperation(value = "알림을 보낼 채널 선택")
    public ResponseEntity<?> getAlertChannelList(@PathVariable("managerId") String managerId) {
        String userId = authService.getMyInfoSecret().getId();
        List<MeetingChannelDto> meetingChannelDtoList = channelUserService.getMeetingChannelByUsers(userId, managerId);
        return ResponseEntity.status(OK).body(meetingChannelDtoList);
    }

}
