package com.meetup.backend.controller;

import com.meetup.backend.dto.channel.ChannelCreateRequestDto;
import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.dto.meetup.*;
import com.meetup.backend.dto.team.TeamActivateRequestDto;
import com.meetup.backend.dto.team.TeamActivateResponseDto;
import com.meetup.backend.dto.team.TeamResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.channel.ChannelService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.meetup.MeetupService;
import com.meetup.backend.service.team.TeamService;
import com.meetup.backend.service.team.TeamUserService;
import com.meetup.backend.service.user.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seungyong on 2022/10/22
 * updated by seongmin on 2022/11/15
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/meetup")
public class MeetUpController {

    private final UserService userService;

    private final TeamUserService teamUserService;

    private final ChannelService channelService;

    private final ChannelUserService channelUserService;

    private final MeetupService meetupService;

    private final AuthService authService;

    // 동기화
    @GetMapping("/sync")
    @ApiOperation(value = "동기화")
    public ResponseEntity<?> registerFromMattermost() {

        String userId = authService.getMyInfoSecret().getId();
        String mmSessionToken = authService.getMMSessionToken(userId);
        userService.registerTeamAndChannel(mmSessionToken, userId);
        return ResponseEntity.status(CREATED).build();
    }

    // 밋업생성할 채널 선택
    @GetMapping("/team")
    @ApiOperation(value = ",해당 로그인 유저의 팀 목록 가져오기")
    public ResponseEntity<List<TeamResponseDto>> getTeamByUserId() {
        List<TeamResponseDto> teamResponseDtoList = teamUserService.getTeamByUser(authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).body(teamResponseDtoList);
    }

    @GetMapping("/channel")
    @ApiOperation(value = "활성화된 모든 팀의 채널목록 반환")
    public ResponseEntity<List<ChannelResponseDto>> getActivatedChannel() {
        List<ChannelResponseDto> channelResponseDtoList = channelUserService.getActivatedChannelByUser(authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).body(channelResponseDtoList);
    }

    // 밋업 CRUD
    @PostMapping
    @ApiOperation(value = "meetup 등록")
    public ResponseEntity<?> registerMeetup(@RequestBody @Valid MeetupRequestDto meetupRequestDto) {
        meetupService.registerMeetUp(meetupRequestDto, authService.getMyInfoSecret().getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{meetupId}")
    @ApiOperation(value = "해당 meetup의 상세정보")
    public ResponseEntity<MeetupUpdateResponseDto> getMeetupInfoById(@PathVariable("meetupId") Long meetupId) {
        MeetupUpdateResponseDto meetupUpdateResponseDto = meetupService.getMeetupInfo(meetupId);
        return ResponseEntity.status(OK).body(meetupUpdateResponseDto);
    }

    @PutMapping("/{meetupId}")
    @ApiOperation(value = "meetup 수정")
    public ResponseEntity<?> updateMeetup(@RequestBody @Valid MeetupUpdateRequestDto meetupUpdateRequestDto, @PathVariable("meetupId") Long meetupId) {
        meetupService.updateMeetup(meetupUpdateRequestDto, authService.getMyInfoSecret().getId(), meetupId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{meetupId}")
    @ApiOperation(value = "meetup 삭제")
    public ResponseEntity<?> deleteMeetup(@PathVariable("meetupId") Long meetupId) {
        meetupService.deleteMeetup(meetupId, authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).build();
    }

    // 사이드바 (해당유저의 밋업, 캘린더)
    @GetMapping("/calendar")
    @ApiOperation(value = "해당 로그인 유저가 참여하고 있는 달력(밋업) 가져오기")
    public ResponseEntity<List<CalendarResponseDto>> getCalendarList() {
        String userId = authService.getMyInfoSecret().getId();
        List<CalendarResponseDto> calendarResponseDtoList = meetupService.getCalendarList(userId, channelUserService.getChannelUserByUser(userId));
        return ResponseEntity.status(OK).body(calendarResponseDtoList);
    }

    @GetMapping("")
    @ApiOperation(value = "해당 로그인 유저의 meetup 목록 가져오기")
    public ResponseEntity<List<MeetupResponseDto>> getMeetupByUserId() {
        String userId = authService.getMyInfoSecret().getId();
        List<MeetupResponseDto> meetupResponseDtoList = meetupService.getResponseDtoList(userId);
        return ResponseEntity.status(OK).body(meetupResponseDtoList);
    }

    @GetMapping("/users/{meetupId}")
    @ApiOperation(value = "밋업에 참가중인 유저의 목록을 반환")
    public ResponseEntity<List<UserInfoDto>> getUserListByMeetup(@PathVariable("meetupId") Long meetupId) {
        Channel channel = meetupService.getMeetupChannelById(meetupId);
        List<UserInfoDto> userInfoDtoList = channelUserService.getMeetupUserByChannel(channel, authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).body(userInfoDtoList);
    }

    // 새로운 채널 생성
    @GetMapping("/userList/{teamId}")
    @ApiOperation(value = "채널 생성 후 참여할 유저 선택을 위해 해당 팀에 참여중인 인원 가져오기")
    public ResponseEntity<List<UserInfoDto>> getUserByTeam(@PathVariable("teamId") String teamId) throws InterruptedException {
        String userId = authService.getMyInfoSecret().getId();
        String mmSessionToken = authService.getMMSessionToken(userId);
        List<UserInfoDto> userInfoDtoList = teamUserService.getUserByTeam(mmSessionToken, teamId);
        return ResponseEntity.status(OK).body(userInfoDtoList);
    }

    @PostMapping("/channel")
    @ApiOperation(value = "새로운 채널 생성")
    public ResponseEntity<?> createNewChannel(@RequestBody ChannelCreateRequestDto channelCreateRequestDto) {
        String userId = authService.getMyInfoSecret().getId();
        String mmSessionToken = authService.getMMSessionToken(userId);
        channelService.createNewChannel(userId, mmSessionToken, channelCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 팀 비활성화
    @GetMapping("/team/activate")
    @ApiOperation(value = "사용자가 참여한 모든 팀 가져오기 (비활성화 된 팀 포함)")
    public ResponseEntity<List<TeamActivateResponseDto>> getAllTeamByUserId() {
        List<TeamActivateResponseDto> teamActivateResponseDtoList = teamUserService.getActivateTeamByUser(authService.getMyInfoSecret().getId());
        return ResponseEntity.status(OK).body(teamActivateResponseDtoList);
    }

    @PutMapping("/team/activate")
    @ApiOperation(value = "팀을 비활성화(밋업 생성시에 표시 안됨)")
    public ResponseEntity<?> activateTeamById(@RequestBody List<TeamActivateRequestDto> teamActivateRequestDtoList) {
        teamUserService.activateTeamUser(authService.getMyInfoSecret().getId(), teamActivateRequestDtoList);
        return ResponseEntity.status(OK).build();
    }
}
