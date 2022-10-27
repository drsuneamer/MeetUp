package com.meetup.backend.service.team;

import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.util.converter.JsonConverter;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/27
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Autowired
    private final TeamRepository teamRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public JSONArray registerTeamFromMattermost(String userId, String mmSessionToken) {

        User user=userRepository.findById(userId).orElseThrow(()->new ApiException(ExceptionEnum.USER_NOT_FOUND));

        MattermostClient client= Client.getClient();
        client.setAccessToken(mmSessionToken);

        Response mmTeamResponse=client.getTeamsForUser(user.getId()).getRawResponse();
        JSONArray teamArray= JsonConverter.toJsonArray((BufferedInputStream) mmTeamResponse.getEntity());

        for(int i=0;i<teamArray.length();i++){

            JSONObject teamObj=teamArray.getJSONObject(i);
            if(teamRepository.findById(teamObj.getString("id")).isEmpty()){
                Team teamEntity=Team.builder()
                        .id(teamObj.getString("id"))
                        .name(teamObj.getString("name"))
                        .displayName(teamObj.getString("display_name"))
                        .type(TeamType.of(teamObj.getString("type")))
                        .build();
                teamRepository.save(teamEntity);
            }

        }

        return teamArray;

    }
}
