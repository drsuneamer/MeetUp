package com.meetup.backend.entity.schedule;

import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import com.meetup.backend.util.converter.LocalDateUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * created by myeongseok on 2022/10/24
 * updated by seongmin on 2022/11/06
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Getter
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime start;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime end;

    private String title;

    private String content;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isOpen;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleType type;

    public Schedule(LocalDateTime start, LocalDateTime end, String title, String content, boolean open, User user) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.content = content;
        this.isOpen = open;
        this.user = user;
    }

    public void update(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        this.start = LocalDateUtil.strToLDT(scheduleUpdateRequestDto.getStart());
        this.end = LocalDateUtil.strToLDT(scheduleUpdateRequestDto.getEnd());
        this.title = scheduleUpdateRequestDto.getTitle();
        this.content = scheduleUpdateRequestDto.getContent();
        this.isOpen = scheduleUpdateRequestDto.isOpen();

    }

    public void update(MeetingUpdateRequestDto meetingUpdateRequestDto) {
        this.start = LocalDateUtil.strToLDT(meetingUpdateRequestDto.getStart());
        this.end = LocalDateUtil.strToLDT(meetingUpdateRequestDto.getEnd());
        this.title = meetingUpdateRequestDto.getTitle();
        this.content = meetingUpdateRequestDto.getContent();
        this.isOpen = meetingUpdateRequestDto.isOpen();

    }
}
