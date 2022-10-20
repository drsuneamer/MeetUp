package com.meetup.backend.entity.team;



import com.meetup.backend.entity.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String displayName;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    @Builder
    public Team(String id, String name, String displayName, RoleType type) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.type = type;
    }
}
