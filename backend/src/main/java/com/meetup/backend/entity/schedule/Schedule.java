package com.meetup.backend.entity.schedule;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * created by myeongseok on 2022/10/24
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

    public Schedule(LocalDateTime start, LocalDateTime end, String title, String content, User user) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.content = content;
        this.isOpen = false;
        this.user = user;
    }

    public Schedule(LocalDateTime start, LocalDateTime end, String title, User user) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.user = user;
    }
}
