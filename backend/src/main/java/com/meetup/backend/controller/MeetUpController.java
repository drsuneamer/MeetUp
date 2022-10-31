package com.meetup.backend.controller;

import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.meetup.MeetupResponseDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.team.Team;
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

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seungyong on 2022/10/22
 * updated by myeongseok on 2022/10/31
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/meetup")
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

    @GetMapping("/channel/{teamId}")
    public ResponseEntity<?> getChannelByUserId(@PathVariable("teamId") String teamId) {
        return ResponseEntity.status(OK).body(channelUserService.getChannelByUser(authService.getMyInfoSecret().getId(), teamId));
    }

    @GetMapping("/sync")
    public ResponseEntity<?> registerFromMattermost() {

        String userId = authService.getMyInfoSecret().getId();
        String mmSessionToken = authService.getMMSessionToken(userId);

        List<Team> teamList = teamService.registerTeamFromMattermost(userId, mmSessionToken);
        teamUserService.registerTeamUserFromMattermost(mmSessionToken, teamList);

        List<Channel> channelList = channelService.registerChannelFromMattermost(userId, mmSessionToken, teamList);
        channelUserService.registerChannelUserFromMattermost(mmSessionToken, channelList);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("")
    public ResponseEntity<?> registerMeetup(@RequestBody @Valid MeetupRequestDto meetupRequestDto) {
        log.info("meetupRequestDto = {}", meetupRequestDto);
        meetupService.registerMeetUp(meetupRequestDto, authService.getMyInfoSecret().getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/calendar")
    public ResponseEntity<?> getCalendarList() {

        String userId = authService.getMyInfoSecret().getId();
        List<MeetupResponseDto> meetupList = meetupService.getCalendarList(channelUserService.getChannelUserByUser(userId));

        return ResponseEntity.status(OK).body(meetupList);

    }

    @PostMapping("/channel/{teamId}")
    public ResponseEntity<?> registerNewChannel(@PathVariable("teamId") String teamId) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/meetup")
    public ResponseEntity<?> getMeetupByUserId() {
        String userId = authService.getMyInfoSecret().getId();
        List<MeetupResponseDto> meetupResponseDtos = meetupService.getResponseDtos(userId);
        return ResponseEntity.status(OK).body(meetupResponseDtos);
    }
}
