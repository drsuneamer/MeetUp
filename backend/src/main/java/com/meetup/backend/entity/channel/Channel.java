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
@Table(name = "CHANNELS")
public class Channel extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String displyName;

    private String URL;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Channel(String name, String displyName, ChannelType type, Team team) {
        this.name = name;
        this.displyName = displyName;
        this.type = type;
        this.team = team;
    }

}
