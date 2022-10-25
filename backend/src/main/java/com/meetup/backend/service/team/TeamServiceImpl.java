package com.meetup.backend.service.team;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.util.converter.JsonConverter;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.util.Optional;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public void registerTeamFromMattermost(String userId, String token) {
        log.info("start register Team From Mattermost");

        MattermostClient client = Client.getClient();
//        client.setAccessToken(token);
        Response mmLoginResponse = client.login("yeonstar1@gmail.com", "Yeon@4302110").getRawResponse();

        switch (mmLoginResponse.getStatus()) {
            case 200:

                JSONObject jsonUserRes = JsonConverter.toJson((BufferedInputStream) mmLoginResponse.getEntity());
                Response mmTeamResponse = client.getTeamsForUser((String) jsonUserRes.get("id")).getRawResponse();
//                Response mmTeamResponse = client.getTeamsForUser(userId).getRawResponse();
                JSONArray teamArray=JsonConverter.toJsonArray((BufferedInputStream) mmTeamResponse.getEntity());
                for (int i = 0; i < teamArray.length(); i++) {
                    JSONObject team = teamArray.getJSONObject(i);
                    if (teamRepository.findById(team.getString("id")).isEmpty()) {

                        Team teamEntity = Team.builder()
                                .id(team.getString("id"))
                                .name(team.getString("name"))
                                .displayName(team.getString("display_name"))
                                .type(TeamType.of(team.getString("type")))
                                .build();
                        teamRepository.save(teamEntity);
                    }
                }

        }

    }
}
