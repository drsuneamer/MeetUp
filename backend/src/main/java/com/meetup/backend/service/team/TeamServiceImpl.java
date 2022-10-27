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
    public void registerTeamFromMattermost(LoginRequestDto requestDto) {
        log.info("start register Team From Mattermost");

        MattermostClient client = Client.getClient();
        Response mmLoginResponse = client.login("yeonstar1@gmail.com", "Yeon@4302110").getRawResponse();

        switch(mmLoginResponse.getStatus()){
            case 200:

                JSONObject jsonUserRes=JsonConverter.toJson((BufferedInputStream) mmLoginResponse.getEntity());
                Response mmTeamResponse= client.getTeamsForUser((String) jsonUserRes.get("id")).getRawResponse();
                JSONTokener tokener=new JSONTokener((BufferedInputStream) mmTeamResponse.getEntity());
                JSONArray teamArray=new JSONArray(tokener);
                for(int i=0;i<teamArray.length();i++){
                    JSONObject team=teamArray.getJSONObject(i);
                    Optional<Team> t=teamRepository.findById(team.getString("id"));

                    log.info((String) team.get("display_name")+teamRepository.findById(team.getString("id")));
                    if(teamRepository.findById(team.getString("id")).get()==null){
                        log.info("=-=-=-={} 저장 시작",team.getString("display_name"));
                        Team teamEntity=Team.builder()
                                .id(team.getString("id"))
                                .name(team.getString("name"))
                                .displayName(team.getString("display_name"))
                                .type(TeamType.valueOf(team.getString("type")))
                                .build();
                        teamRepository.save(teamEntity);
                        log.info("========{} 저장 완료",teamEntity.getDisplayName());
                    }
                }
                log.info("팀 목록 저장 완료");

        }

    }
}
