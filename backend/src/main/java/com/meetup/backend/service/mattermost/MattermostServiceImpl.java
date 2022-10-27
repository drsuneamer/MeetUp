package com.meetup.backend.service.mattermost;

import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
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
import java.util.List;

/**
 * created by seungyong on 2022/10/27
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MattermostServiceImpl implements MattermostService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelUserRepository channelUserRepository;

    @Override
    public void registerTeamAndChannelById(String userId, String mmSessionToken) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));

        List<TeamUser> teamUserList = teamUserRepository.findByUser(user);

        if (teamUserList.isEmpty()) {
            registerMattermost(userId, mmSessionToken);
        }

    }

    @Override
    public void registerMattermost(String userId, String mmSessionToken) {

        userRepository.save(User.builder().id(userId).build());

        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        Response mmTeamResponse = client.getTeamsForUser(userId).getRawResponse();
        JSONArray teamArray = JsonConverter.toJsonArray((BufferedInputStream) mmTeamResponse.getEntity());
        for (int i = 0; i < teamArray.length(); i++) {
            JSONObject teamObj = teamArray.getJSONObject(i);
            if (teamRepository.findById(teamObj.getString("id")).isEmpty()) {

                Team teamEntity = Team.builder()
                        .id(teamObj.getString("id"))
                        .name(teamObj.getString("name"))
                        .displayName(teamObj.getString("display_name"))
                        .type(TeamType.of(teamObj.getString("type")))
                        .build();
                teamRepository.save(teamEntity);
            }

            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
            Team team = teamRepository.findById(teamObj.getString("id")).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
            TeamUser teamUser = TeamUser.builder()
                    .team(team)
                    .user(user)
                    .build();

            teamUserRepository.save(teamUser);

            Response mmChannelResponse = client.getChannelsForTeamForUser(team.getId(), user.getId()).getRawResponse();
            JSONArray channelArray = JsonConverter.toJsonArray((BufferedInputStream) mmChannelResponse.getEntity());
            for (int k = 0; k < channelArray.length(); k++) {
                JSONObject channelObj = channelArray.getJSONObject(k);
                if (channelObj.getString("team_id").equals(""))
                    continue;
                if (channelRepository.findById(channelObj.getString("id")).isEmpty()) {

                    Channel channelEntity = Channel.builder()
                            .id(channelObj.getString("id"))
                            .name(channelObj.getString("name"))
                            .displyName(channelObj.getString("display_name"))
                            .type(ChannelType.of(teamObj.getString("type")))
                            .team(Team.builder().id(channelObj.getString("team_id")).build())
                            .build();
                    channelRepository.save(channelEntity);

                }

                Channel channel = channelRepository.findById(channelObj.getString("id")).orElseThrow(() -> new ApiException(ExceptionEnum.CHANNEL_NOTFOUND));
                ChannelUser channelUser = ChannelUser.builder()
                        .channel(channel)
                        .user(user)
                        .build();

                channelUserRepository.save(channelUser);
            }

        }


    }

}
