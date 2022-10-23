package com.meetup.backend.service.team;

import com.meetup.backend.dto.team.TeamDto;

import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/22
 */
public interface TeamUserService {

    public List<TeamDto> getTeamByUser(String userId);

}
