package com.meetup.backend.service.mattermost;

/**
 * created by seungyong on 2022/10/27
 */
public interface MattermostService {
    void registerTeamAndChannelById(String userId, String mmSessionToken);
    void registerMattermost(String userId, String mmSessionToken);

    void registerTeamById(String userId, String mmSessionToken);
    void registerChannelByIdAndTeam(String userId, String teamId, String mmSessionToken);
}
