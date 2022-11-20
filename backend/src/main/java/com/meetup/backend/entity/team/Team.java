package com.meetup.backend.entity.team;


import com.meetup.backend.entity.*;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * created by seungyong on 2022/10/20
 * updated by seungyong on 2022/11/06
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String displayName;

    @Enumerated(EnumType.STRING)
    private TeamType type;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<TeamUser> teamUsers;

    @Builder
    public Team(String id, String name, String displayName, TeamType type) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.type = type;
    }
}
