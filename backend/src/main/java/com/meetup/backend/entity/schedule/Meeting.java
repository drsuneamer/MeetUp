package com.meetup.backend.entity.schedule;

import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * created by seongmin on 2022/10/20
 * updated by seongmin on 2022/11/06
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meeting extends Schedule {

    @ManyToOne
    @JoinColumn(name = "meetup_id")
    private Meetup meetup;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Builder
    public Meeting(LocalDateTime start, LocalDateTime end, String title, String content, boolean open, User user, Meetup meetup, Party party) {
        super(start, end, title, content, open, user);
        this.meetup = meetup;
        this.party = party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public void update(MeetingUpdateRequestDto meetingUpdateRequestDto, Meetup meetup) {
        this.meetup = meetup;
        super.update(meetingUpdateRequestDto);
    }
}
