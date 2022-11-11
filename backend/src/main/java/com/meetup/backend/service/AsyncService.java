package com.meetup.backend.service;

import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.dto.user.UserListInTeamResponseDto;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.JsonConverter;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
        }
        return new AsyncResult<>(user);
    }

    @Async
    public ListenableFuture<String> print(String message, CountDownLatch latch) throws InterruptedException {
        log.info("Task Start - {}", message);
        log.info("1thread name = {}", Thread.currentThread().getName());
        Thread.sleep(2000);
        log.info("thread.sleep 종료 latch count = {}", latch.getCount());
        latch.countDown();
        log.info("thread.sleep 종료 latch countDown 후 latch count = {}", latch.getCount());

        return new AsyncResult<>("meetup-" + message);
    }
}
