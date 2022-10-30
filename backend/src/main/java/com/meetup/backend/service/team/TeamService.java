package com.meetup.backend.service.team;


import com.meetup.backend.entity.team.Team;

import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/27
 * updated by seungyong on 2022/10/28
 */
public interface TeamService {

    List<Team> registerTeamFromMattermost(String userId, String mmSessionToken);

}
