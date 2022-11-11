package com.meetup.backend.service;

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
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * created by seongmin on 2022/11/10
 */
@Service
@Slf4j
public class AsyncService {
    @Async
    public CompletableFuture<UserListInTeamResponseDto> getNickname(MattermostClient client, UserListInTeamResponseDto user) {
        Response response = client.getUser(user.getId()).getRawResponse();
        JSONObject userObj = new JSONObject();
        try {
            userObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());

        } catch (ClassCastException e) {
            log.error(e.getMessage());
            log.info("response.getEntity() = {}", response.getEntity());
            e.printStackTrace();
        }
        user.setNickname(userObj.getString("nickname"));
        log.info("nickname = {}", user.getNickname());

        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<List<UserListInTeamResponseDto>> getResult(MattermostClient client, List<TeamUser> teamUserList) {

        List<UserListInTeamResponseDto> result = new ArrayList<>();
        for (TeamUser teamUser : teamUserList) {
            User user = teamUser.getUser();
            UserListInTeamResponseDto userListInTeamResponseDto = UserListInTeamResponseDto.of(user);

            if (user.getNickname() != null) {
                continue;
            }

            Response response = client.getUser(user.getId()).getRawResponse();
            JSONObject userObj = new JSONObject();
            try {
                userObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());

            } catch (ClassCastException e) {
                log.error(e.getMessage());
                log.info("response.getEntity() = {}", response.getEntity());
//                e.printStackTrace();
            }
            try {
                userListInTeamResponseDto.setNickname(userObj.getString("nickname"));
            } catch (JSONException e) {
                log.error(e.getMessage());
//                e.printStackTrace();
            }

            result.add(userListInTeamResponseDto);
        }
        return CompletableFuture.completedFuture(result);

    }
}
