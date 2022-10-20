package com.meetup.backend.entity.team;

import com.meetup.backend.entity.*;
import com.meetup.backend.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamUser extends BaseEntity{
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public TeamUser(String id, Team team, User user) {
        this.id = id;
        this.team = team;
        this.user = user;
    }
}
