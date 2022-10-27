package com.meetup.backend.controller;

import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.meetup.TeamChannelResponseDto;
import com.meetup.backend.dto.team.TeamResponseDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.mattermost.MattermostService;
import com.meetup.backend.service.meetup.MeetupService;
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
 * updated by seungyong on 2022/10/27
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/meetup")
public class MeetUpController {

    @Autowired
    private final TeamUserService teamUserService;
    @Autowired
    private final MattermostService mattermostService;
    @Autowired
    private final ChannelUserService channelUserService;
    @Autowired
    private final MeetupService meetupService;

    @Autowired
    private final AuthService authService;


    @GetMapping("")
    public ResponseEntity<?> getTeamAndChannelByUserId() {

        String userId = authService.getMyInfoSecret().getId();
        String token = authService.getMMSessionToken(authService.getMyInfoSecret().getId());
        mattermostService.registerTeamAndChannelById(userId, token);
        List<TeamResponseDto> teamRes = teamUserService.getTeamByUser(userId);
        TeamChannelResponseDto response = TeamChannelResponseDto.builder()
                .teamList(teamRes)
//                .channelList(channelUserService.getChannelByUser(userId))
                .build();

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/team")
    public ResponseEntity<?> getTeamByUserId(){
        return ResponseEntity.status(OK).body(null);
    }

    @GetMapping("/team/sync")
    public ResponseEntity<?> getTeamSyncByUserId(){
        return ResponseEntity.status(OK).body(null);
    }

    @GetMapping("/channel/{teamId}")
    public ResponseEntity<?> getChannelByUserId(@PathVariable("teamId") String teamId){
        return ResponseEntity.status(OK).body(null);
    }

    @GetMapping("/channel/sync/{teamId}")
    ResponseEntity<?> getChannelSyncByUserId(@PathVariable("teamId") String teamId){
        return ResponseEntity.status(OK).body(null);
    }

    @PostMapping("channel/{teamId}")
    ResponseEntity<?> registerNewChannel(@PathVariable("teamid") String teamId){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping
    public ResponseEntity<?> registerMeetup(@RequestBody @Valid MeetupRequestDto meetupRequestDto) {
        log.info("meetupRequestDto = {}", meetupRequestDto);
        meetupService.registerMeetUp(meetupRequestDto, "사용자ID");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
