package com.meetup.backend.entity.meeting;


import com.meetup.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * created by myeongseok on 2022/10/24
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User joinUser;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Builder
    public Group(User user, Meeting meeting) {
        this.joinUser = user;
        this.meeting = meeting;
    }
}
