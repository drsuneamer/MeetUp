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
public class TeamDto {

    private String id;
    private String name;
    private String displayName;
    private TeamType type;

    public static TeamDto of(Team team){
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .displayName(team.getDisplayName())
                .type(team.getType())
                .build();
    }

}
