package com.meetup.backend.controller;

import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.channel.ChannelService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.meetup.MeetupService;
import com.meetup.backend.service.team.TeamService;
import com.meetup.backend.service.team.TeamUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seungyong on 2022/10/22
 * updated by seungyong on 2022/10/27
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/meetup")
public class MeetUpController {

    @Autowired
    private final TeamService teamService;

    @Autowired
    private final TeamUserService teamUserService;

    @Autowired
    private final ChannelService channelService;

    @Autowired
    private final ChannelUserService channelUserService;

    @Autowired
    private final MeetupService meetupService;

    @Autowired
    private final AuthService authService;

    @GetMapping("/team")
    public ResponseEntity<?> getTeamByUserId() {
        return ResponseEntity.status(OK).body(teamUserService.getTeamByUser(authService.getMyInfoSecret().getId()));
    }

    @GetMapping("/team/sync")
    public ResponseEntity<?> getTeamSyncByUserId() {

        String userId = authService.getMyInfoSecret().getId();
        String token = authService.getMMSessionToken(userId);

        teamUserService.registerTeamUserFromMattermost(token, teamService.registerTeamFromMattermost(userId, token));

        return ResponseEntity.status(OK).body(teamUserService.getTeamByUser(authService.getMyInfoSecret().getId()));
    }

    @GetMapping("/channel/{teamId}")
    public ResponseEntity<?> getChannelByUserId(@PathVariable("teamId") String teamId) {
        return ResponseEntity.status(OK).body(channelUserService.getChannelByUser(authService.getMyInfoSecret().getId(), teamId));
    }

    @GetMapping("/channel/sync/{teamId}")
    ResponseEntity<?> getChannelSyncByUserId(@PathVariable("teamId") String teamId) {

        String userId = authService.getMyInfoSecret().getId();
        String token = authService.getMMSessionToken(userId);

        channelUserService.registerChannelUserFromMattermost(token, channelService.registerChannelFromMattermost(userId, teamId, token));

        return ResponseEntity.status(OK).body(channelUserService.getChannelByUser(authService.getMyInfoSecret().getId(), teamId));
    }

    @PostMapping("channel/{teamId}")
    ResponseEntity<?> registerNewChannel(@PathVariable("teamId") String teamId) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping
    public ResponseEntity<?> registerMeetup(@RequestBody @Valid MeetupRequestDto meetupRequestDto) {
        log.info("meetupRequestDto = {}", meetupRequestDto);
        meetupService.registerMeetUp(meetupRequestDto, "사용자ID");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
