package com.meetup.backend.service;

import net.bis5.mattermost.client4.MattermostClient;

import java.util.logging.Level;

/**
 * created by seungyong on 2022/10/21
 * updated by seongmin on 2022/10/23
 */
public class Client {

    public static MattermostClient getClient() {
        return MattermostClient.builder()
                .url("https://meeting.ssafy.com")
                .logLevel(Level.INFO)
                .ignoreUnknownProperties()
                .build();
    }

}
