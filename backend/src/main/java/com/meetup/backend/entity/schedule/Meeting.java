package com.meetup.backend.entity.schedule;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.group.Group;
import com.meetup.backend.entity.meetup.Meetup;
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
 * updated by myeongseok on 2022/10/25
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meeting extends Schedule {

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "meetup_id")
    private Meetup meetup;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Meeting(LocalDateTime start, LocalDateTime end, String title, String content, boolean isOpen, User user, User applicant, Meetup meetup, Group group) {
        super(start, end, title, content, isOpen, user);
        this.applicant = applicant;
        this.meetup = meetup;
        this.group = group;
    }
}
