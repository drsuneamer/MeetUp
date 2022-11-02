package com.meetup.backend.entity.channel;

import java.io.Serializable;

/**
 * created by myeongseok on 2022/11/02
 */
public class ChannelUserId implements Serializable {

    private String channel;

    private String user;

    public ChannelUserId() {
    }

    public ChannelUserId(String channel, String user) {
        this.channel = channel;
        this.user = user;
    }
}
