package com.meetup.backend.entity.party;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.schedule.Meeting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * created by seongmin on 2022/11/08
 * updated by seongmin on 2022/11/10
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PartyMeeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime start;

    @Builder
    public PartyMeeting(Meeting meeting, Party party, LocalDateTime start) {
        this.meeting = meeting;
        this.party = party;
        this.start = start;
    }
}
