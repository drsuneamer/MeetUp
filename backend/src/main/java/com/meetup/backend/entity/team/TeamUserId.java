package com.meetup.backend.entity.team;

import java.io.Serializable;

/**
 * created by myeongseok on 2022/11/02
 */
public class TeamUserId implements Serializable {
    private String team;
    private String user;

    public TeamUserId() {
    }

    public TeamUserId(String team, String user) {
        this.team = team;
        this.user = user;
    }
}
