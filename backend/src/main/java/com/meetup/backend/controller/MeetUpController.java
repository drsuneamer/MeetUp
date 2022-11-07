package com.meetup.backend.controller;

import com.meetup.backend.dto.meetup.*;
import com.meetup.backend.dto.team.TeamActivateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.channel.ChannelService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.meetup.MeetupService;
import com.meetup.backend.service.team.TeamService;
import com.meetup.backend.service.team.TeamUserService;
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
 * updated by seungyong on 2022/11/05
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/meetup")
public class MeetUpController {

    private final TeamService teamService;

    private final TeamUserService teamUserService;

    private final ChannelService channelService;

    private final ChannelUserService channelUserService;

    private final MeetupService meetupService;

    private final AuthService authService;

    @GetMapping("/team")
    @ApiOperation(value = ",해당 로그인 유저의 팀 목록 가져오기")
    public ResponseEntity<?> getTeamByUserId() {
        return ResponseEntity.status(OK).body(teamUserService.getTeamByUser(authService.getMyInfoSecret().getId()));
    }

    @GetMapping("/channel/{teamId}")
    @ApiOperation(value = "팀 ID에 해당하는 채널 목록 가져오기")
    public ResponseEntity<?> getChannelByUserId(@PathVariable("teamId") String teamId) {
        return ResponseEntity.status(OK).body(channelUserService.getChannelByUser(authService.getMyInfoSecret().getId(), teamId));
    }

    @GetMapping("/sync")
    @ApiOperation(value = "동기화")
    public ResponseEntity<?> registerFromMattermost() {

        String userId = authService.getMyInfoSecret().getId();
        String mmSessionToken = authService.getMMSessionToken(userId);

        List<Team> teamList = teamService.registerTeamFromMattermost(userId, mmSessionToken);
        teamUserService.registerTeamUserFromMattermost(mmSessionToken, teamList);

        List<Channel> channelList = channelService.registerChannelFromMattermost(userId, mmSessionToken, teamList);
        channelUserService.registerChannelUserFromMattermost(mmSessionToken, channelList);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping
    @ApiOperation(value = "meetup 등록")
    public ResponseEntity<?> registerMeetup(@RequestBody @Valid MeetupRequestDto meetupRequestDto) {
        meetupService.registerMeetUp(meetupRequestDto, authService.getMyInfoSecret().getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @GetMapping("/calendar")
    @ApiOperation(value = "해당 로그인 유저가 참여하고 있는 달력(밋업) 가져오기")
    public ResponseEntity<?> getCalendarList() {

        String userId = authService.getMyInfoSecret().getId();
        List<CalendarResponseDto> calendarResponseDtoList = meetupService.getCalendarList(authService.getMyInfoSecret().getId(), channelUserService.getChannelUserByUser(userId));

        return ResponseEntity.status(OK).body(calendarResponseDtoList);

    }

    @PostMapping("/channel/{teamId}")
    @ApiOperation(value = "팀 ID에 속하는 채널을 등록")

    public ResponseEntity<?> registerNewChannel(@PathVariable("teamId") String teamId) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    @ApiOperation(value = "해당 로그인 유저의 meetup 목록 가져오기")
    public ResponseEntity<?> getMeetupByUserId() {
        String userId = authService.getMyInfoSecret().getId();
        List<MeetupResponseDto> meetupResponseDtos = meetupService.getResponseDtos(userId);
        return ResponseEntity.status(OK).body(meetupResponseDtos);
    }

    @GetMapping("/{meetupId}")
    @ApiOperation(value = "해당 meetup의 상세정보")
    public ResponseEntity<?> getMeetupInfoById(@PathVariable("meetupId") Long meetupId) {
        return ResponseEntity.status(OK).body(meetupService.getMeetupInfo(meetupId));
    }

    @GetMapping("/users/{meetupId}")
    @ApiOperation(value = "밋업에 참가중인 유저의 목록을 반환")
    public ResponseEntity<?> getUserListByMeetup(@PathVariable("meetupId") Long meetupId) {

        Channel channel = meetupService.getMeetupChannelById(meetupId);
        MeetupUserResponseDto responseDto = MeetupUserResponseDto.of(channelUserService.getMeetupUserByChannel(channel, authService.getMyInfoSecret().getId()));

        return ResponseEntity.status(OK).body(responseDto);
    }

    @GetMapping("/team/activate")
    @ApiOperation(value = "사용자가 참여한 모든 팀 가져오기 (비활성화 된 팀 포함)")
    public ResponseEntity<?> getAllTeamByUserId() {

        return ResponseEntity.status(OK).body(teamUserService.getActivateTeamByUser(authService.getMyInfoSecret().getId()));
    }

    @PutMapping("/team/activate")
    @ApiOperation(value = "팀을 비활성화(밋업 생성시에 표시 안됨)")
    public ResponseEntity<?> activateTeamById(@RequestBody List<TeamActivateRequestDto> teamActivateRequestDtoList) {

        teamUserService.activateTeamUser(authService.getMyInfoSecret().getId(), teamActivateRequestDtoList);

        return ResponseEntity.status(OK).build();
    }
}
