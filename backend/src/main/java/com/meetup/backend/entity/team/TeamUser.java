package com.meetup.backend.entity.team;

import com.meetup.backend.entity.*;
import com.meetup.backend.entity.user.User;
import lombok.*;

import javax.persistence.*;

/**
 * created by seungyoung on 2022/10/20
 * updated by seongmin on 2022/10/21
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public TeamUser(Team team, User user) {
        this.team = team;
        this.user = user;
    }
}
