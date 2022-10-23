package com.meetup.backend.controller;

import com.meetup.backend.dto.channel.ChannelDto;
import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.team.TeamDto;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.service.channel.ChannelService;
import com.meetup.backend.service.team.TeamUserService;
import com.meetup.backend.service.user.UserService;
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
    private final ChannelService channelService;

    @GetMapping("/team/{userId}")
    public ResponseEntity<?> getTeamByUserId(@PathVariable("userId") String userId){
        System.out.println("==============================================");
        log.info("getUserId = {}", userId);

        List<TeamDto> teamDtoList=teamUserService.getTeamByUser(userId);

        return ResponseEntity.status(OK).body(teamDtoList);
    }

    @GetMapping("/channel/{teamId}")
    public ResponseEntity<?> getChannel(@PathVariable("teamId") String teamId){
        log.info("getTeamId = {}",teamId);

        List<ChannelDto> channelDtoList=channelService.getChannelByTeam(teamId);

        return ResponseEntity.status(OK).body(channelDtoList);
    }

    @PostMapping
    public ResponseEntity<?> registerMeetup(@RequestBody @Valid MeetupRequestDto meetupRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
