package com.meetup.backend.controller;

import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.team.TeamResponseDto;
import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.channel.ChannelService;
import com.meetup.backend.service.meetup.MeetupService;
import com.meetup.backend.service.team.TeamService;
import com.meetup.backend.service.team.TeamUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seungyong on 2022/09/22
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/meetup")
public class MeetUpController {

    @Autowired
    private final TeamUserService teamUserService;
    @Autowired
    private final TeamService teamService;
    @Autowired
    private final ChannelService channelService;
    @Autowired
    private final MeetupService meetupService;

    @Autowired
    private final AuthService authService;


    @GetMapping("/team")
    public ResponseEntity<?> getTeamByUserId() {

//        teamService.registerTeamFromMattermost(authService.getMyInfoSecret().getId(),authService.getMMSessionToken(authService.getMyInfoSecret().getId()));
        teamService.registerTeamFromMattermost("pfnfdm4febgd5qmzemdu91ri6w","");

//        List<TeamResponseDto> teamUserList=teamUserService.getTeamByUser("pfnfdm4febgd5qmzemdu91ri6w");

//        return ResponseEntity.status(OK).body(teamUserList);
        return ResponseEntity.status(OK).body(null);
    }

    @GetMapping("/channel/{teamId}")
    public ResponseEntity<?> getChannel(@PathVariable("teamId") String teamId) {
        log.info("getTeamId = {}", teamId);

        List<ChannelResponseDto> channelResponseDtoList = channelService.getChannelByTeam(teamId);

        return ResponseEntity.status(OK).body(channelResponseDtoList);
    }

    @PostMapping
    public ResponseEntity<?> registerMeetup(@RequestBody @Valid MeetupRequestDto meetupRequestDto) {
        log.info("meetupRequestDto = {}", meetupRequestDto);
        meetupService.registerMeetUp(meetupRequestDto, "사용자ID");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
