package com.meetup.backend.service.team;

import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.util.converter.JsonConverter;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Team;
import net.bis5.mattermost.model.TeamList;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;

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
    public void registerTeamFromMattermost() {
        log.info("start register Team From Mattermost");

        MattermostClient client = Client.getClient();
//        client.setAccessToken();
        Response mmLoginResponse = client.login("yeonstar1@gmail.com", "Yeon@4302110").getRawResponse();
        JSONObject jsonRes = JsonConverter.toJson((BufferedInputStream) mmLoginResponse.getEntity());

        ApiResponse<TeamList> teamList = client.getTeamsForUser("pfnfdm4febgd5qmzemdu91ri6w");
        for (Team team : teamList.readEntity()) {
            log.info("=====team = {}", team.getId());
            log.info("=======test = {}", teamRepository.findById(team.getId()));
            log.info("========display = {}", team.getDisplayName());
            if (teamRepository.findById(team.getId()) == null) {

                com.meetup.backend.entity.team.Team teamEntity = com.meetup.backend.entity.team.Team.builder()
                        .id(team.getId())
                        .type(TeamType.of(team.getType().getCode()))
                        .name(team.getName())
                        .displayName(team.getDisplayName())
                        .build();

                teamRepository.save(teamEntity);

            }
        }
        log.info("end register Team From Mattermost");

    }
}
