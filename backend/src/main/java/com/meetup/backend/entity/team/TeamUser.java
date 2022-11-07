package com.meetup.backend.entity.team;

import com.meetup.backend.entity.*;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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

    @Override
    public boolean equals(Object obj) {
        if (this.team.equals(((TeamUser) obj).getTeam()) && this.user.getId().equals(((TeamUser) obj).getUser().getId()))
            return true;
        return false;
    }

    public void changeActivate() {
        this.isActivate = !this.isActivate;
    }
}
