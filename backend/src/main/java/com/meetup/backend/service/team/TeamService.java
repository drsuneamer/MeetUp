package com.meetup.backend.service.team;

import com.meetup.backend.dto.user.LoginRequestDto;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/24
 */
public interface TeamService {

    public void registerTeamFromMattermost(String userId, String token);

}
