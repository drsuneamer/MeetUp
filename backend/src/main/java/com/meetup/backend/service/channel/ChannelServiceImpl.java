package com.meetup.backend.service.channel;

import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
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

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/23
 * updated by seungyong on 2022/10/27
 * updated by seungyong on 2022/10/30
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private final ChannelRepository channelRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<Channel> registerChannelFromMattermost(String userId, String mmSessionToken, List<Team> teamList) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        List<Channel> channelList = new ArrayList<>();

        for (Team team : teamList) {

            Response mmChannelResponse = client.getChannelsForTeamForUser(team.getId(), user.getId()).getRawResponse();
            JSONArray channelArray = JsonConverter.toJsonArray((BufferedInputStream) mmChannelResponse.getEntity());

            for (int i = 0; i < channelArray.length(); i++) {

                JSONObject channelObj = channelArray.getJSONObject(i);
                if (channelObj.getString("type").equals("D") || channelObj.getString("type").equals("G")) continue;

                Channel channel;
                if (channelRepository.findById(channelObj.getString("id")).isEmpty()) {
                    channel = Channel.builder()
                            .id(channelObj.getString("id"))
                            .team(team)
                            .name(channelObj.getString("name"))
                            .displyName(channelObj.getString("display_name"))
                            .type(ChannelType.of(channelObj.getString("type")))
                            .build();
                    channelRepository.save(channel);
                } else {
                    channel = channelRepository.findById(channelObj.getString("id")).get();
                }
                channelList.add(channel);

            }

        }
        return channelList;
    }
}
