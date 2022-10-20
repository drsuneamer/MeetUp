package com.meetup.backend.entity.channel;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "CHANNEL_USER")
public class ChannelUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Builder
    public ChannelUser(User user, Channel channel) {
        this.user = user;
        this.channel = channel;

    }

}
