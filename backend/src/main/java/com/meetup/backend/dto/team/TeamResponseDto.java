package com.meetup.backend.dto.team;

import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import lombok.Builder;
import lombok.Data;

/**
 * created by seungyong on 2022/10/22
 * updated by seungyong on 2022/10/22
 */
@Data
@Builder
public class TeamResponseDto {

    private String categoryId;
    private String displayName;

    public static TeamResponseDto of(Team team) {
        return TeamResponseDto.builder()
                .categoryId(team.getId())
                .displayName(team.getDisplayName())
                .build();
    }

}
