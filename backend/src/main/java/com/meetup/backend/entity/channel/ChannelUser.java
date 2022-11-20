package com.meetup.backend.entity.channel;

import com.meetup.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * created by myeongseok on 2022/10/20
 * updated by seongmin on 2022/11/02
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(ChannelUserId.class)
public class ChannelUser implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelUser that = (ChannelUser) o;
        return Objects.equals(user, that.user) && Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, channel);
    }
}
