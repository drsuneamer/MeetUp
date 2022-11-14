package com.meetup.backend.entity.meetup;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * created by seongmin on 2022/10/20
 * updated by seungyong on 2022/11/01
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meetup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String color;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "meetup", fetch = FetchType.LAZY)
    private List<Meeting> meetings;

    public void changeMeetup(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public void deleteMeetup() {
        this.isDelete = true;
    }

    public void reviveMeetup(String title, String color) {
        this.isDelete = false;
        this.title = title;
        this.color = color;
    }

    @Builder
    public Meetup(String title, String color, User manager, Channel channel) {
        this.title = title;
        this.color = color;
        this.manager = manager;
        this.channel = channel;
        this.isDelete = false;
    }
}
