package com.meetup.backend.entity.meetup;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
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
