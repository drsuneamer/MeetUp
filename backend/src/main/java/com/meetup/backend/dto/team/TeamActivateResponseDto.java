package com.meetup.backend.dto.team;

import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * created by seungyong on 2022/11/06
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamActivateResponseDto {

    private String id;

    private String displayName;

    private Boolean isActivate;

    public static TeamActivateResponseDto of(TeamUser teamUser) {
        return new TeamActivateResponseDto(teamUser.getTeam().getId(), teamUser.getTeam().getDisplayName(), teamUser.isActivate());
    }

}
