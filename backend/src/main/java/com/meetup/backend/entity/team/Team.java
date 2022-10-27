package com.meetup.backend.entity.team;


import com.meetup.backend.entity.*;
import lombok.*;

import javax.persistence.*;

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

    @Builder
    public Team(String id, String name, String displayName, TeamType type) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.type = type;
    }
}
