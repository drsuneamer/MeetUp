package com.meetup.backend.entity.channel;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.team.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/**
 * created by myeongseok on 2022/10/20
 * updated by myeongseok on 2022/10/20
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Channel extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String displayName;

    private String URL;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Channel(String id, String name, String displyName, ChannelType type, Team team) {
        this.id = id;
        this.name = name;
        this.displayName = displyName;
        this.type = type;
        this.team = team;
    }

}
