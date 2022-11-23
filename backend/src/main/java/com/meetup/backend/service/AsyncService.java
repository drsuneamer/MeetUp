package com.meetup.backend.service;

import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.util.converter.JsonConverter;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.BufferedInputStream;
import java.util.concurrent.CountDownLatch;

/**
 * created by seongmin on 2022/11/10
 * updated by seongmin on 2022/11/11
 */
@Service
@Slf4j
public class AsyncService {
    @Async
    public ListenableFuture<UserInfoDto> getNickname(MattermostClient client, UserInfoDto user, CountDownLatch latch) {
        Response response = client.getUser(user.getId()).getRawResponse();
        JSONObject userObj;
        try {
            userObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());
            user.setNickname(userObj.getString("nickname"));
        } catch (ClassCastException e) {
            response = client.getUser(user.getId()).getRawResponse();
            userObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());
            user.setNickname(userObj.getString("nickname"));
            return new AsyncResult<>(user);
        } finally {
            latch.countDown();
        }
        return new AsyncResult<>(user);
    }
}
