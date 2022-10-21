package com.meetup.backend.service;

import net.bis5.mattermost.client4.MattermostClient;

import java.util.logging.Level;

/**
 * created by seungyong on 2022/10/21
 */
public class Client {

    public static MattermostClient getClient(String sessionToken){
        MattermostClient client= MattermostClient.builder()
                .url("https://meeting.ssafy.com")
                .logLevel(Level.INFO)
                .ignoreUnknownProperties()
                .build();
        client.setAccessToken(sessionToken);
        return client;
    }

}
