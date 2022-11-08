package com.meetup.backend.entity.party;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * created by seongmin on 2022/11/08
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PartySchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Builder
    public PartySchedule(Schedule schedule, Party party) {
        this.schedule = schedule;
        this.party = party;
    }
}
