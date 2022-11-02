package com.meetup.backend.entity.channel;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * created by myeongseok on 2022/10/20
 * updated by seongmin on 2022/11/02
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(ChannelUserId.class)
public class ChannelUser extends BaseEntity implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Builder
    public ChannelUser(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }

}
