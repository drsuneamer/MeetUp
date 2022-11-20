package com.meetup.backend.dto.team;

import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import lombok.*;

/**
 * created by seungyong on 2022/10/22
 * updated by seungyong on 2022/11/06
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamResponseDto {

    private String id;

    private String displayName;

    private TeamType type;

    public static TeamResponseDto of(Team team) {
        return new TeamResponseDto(team.getId(), team.getDisplayName(), team.getType());
    }
}
