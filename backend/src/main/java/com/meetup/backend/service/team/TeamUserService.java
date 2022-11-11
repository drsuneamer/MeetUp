package com.meetup.backend.service.team;

import com.meetup.backend.dto.team.TeamActivateRequestDto;
import com.meetup.backend.dto.team.TeamActivateResponseDto;
import com.meetup.backend.dto.team.TeamResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.dto.user.UserListInTeamResponseDto;
import com.meetup.backend.entity.team.Team;

import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/11/05
 */
public interface TeamUserService {

    List<TeamResponseDto> getTeamByUser(String userId);

    void registerTeamUserFromMattermost(String mmSessionToken, List<Team> teamList);

    List<TeamActivateResponseDto> getActivateTeamByUser(String userId);

    void activateTeamUser(String teamId, List<TeamActivateRequestDto> teamActivateRequestDtoList);

    List<UserInfoDto> getUserByTeam(String mmSessionToken, String teamId) throws InterruptedException;

}
