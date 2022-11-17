package com.meetup.backend.entity.team;

import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * created by seungyong on 2022/10/20
 * updated by seungyong on 2022/11/06
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(TeamUserId.class)
public class TeamUser implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isActivate;

    @Id
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public TeamUser(Team team, User user) {
        this.team = team;
        this.user = user;
        isActivate = true;
    }

    public void changeActivate() {
        this.isActivate = !this.isActivate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamUser teamUser = (TeamUser) o;
        return Objects.equals(team, teamUser.team) && Objects.equals(user, teamUser.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, user);
    }
}
