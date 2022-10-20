package com.meetup.backend.entity.meeting;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * created by seongmin on 2022/10/20
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @Builder
    public Meeting(Date start, Date end, String title, String content, User manager, User applicant) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.content = content;
        this.manager = manager;
        this.applicant = applicant;
    }
}
