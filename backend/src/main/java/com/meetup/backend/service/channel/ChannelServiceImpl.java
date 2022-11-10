package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelCreateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.exception.MattermostEx;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seongmin on 2022/11/04
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private final ChannelRepository channelRepository;

    @Autowired
    private final ChannelUserRepository channelUserRepository;

    @Autowired
    private final UserRepository userRepository;

    /**
     * @return Channel DB에 저장되어 있지 않은 Channel 리스트
     */
    @Override
    public List<Channel> registerChannelFromMattermost(String userId, String mmSessionToken, List<Team> teamList) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        List<Channel> channelList = new ArrayList<>();

        for (Team team : teamList) {

            Response mmChannelResponse = client.getChannelsForTeamForUser(team.getId(), user.getId()).getRawResponse();

            MattermostEx.apiException(mmChannelResponse.getStatus());
            JSONArray channelArray = new JSONArray();
            try {
                channelArray = JsonConverter.toJsonArray((BufferedInputStream) mmChannelResponse.getEntity());
            } catch (ClassCastException e) {
                log.error(e.getMessage());
                log.info("mmChannelUserResponse.getEntity() = {}", mmChannelResponse.getEntity());
                e.printStackTrace();
            }

            for (int i = 0; i < channelArray.length(); i++) {

                JSONObject channelObj = channelArray.getJSONObject(i);
                if (channelObj.getString("type").equals("D") || channelObj.getString("type").equals("G")) continue;


                Channel channel = channelRepository.findById(channelObj.getString("id"))
                        .orElseGet(() -> channelRepository.save(Channel.builder()
                                .id(channelObj.getString("id"))
                                .team(team)
                                .name(channelObj.getString("name"))
                                .displyName(channelObj.getString("display_name"))
                                .type(ChannelType.of(channelObj.getString("type")))
                                .build()));
                channelList.add(channel);
            }
        }
        return channelList;
    }

    @Transactional
    @Override
    public void createNewChannel(String userId, String mmSessionToken, ChannelCreateRequestDto channelCreateRequestDto) {

        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        if (channelRepository.existsByName(channelCreateRequestDto.getName())) {
            throw new ApiException(ExceptionEnum.DUPLICATE_CHANNEL_NAME);
        }

        net.bis5.mattermost.model.Channel channel = new net.bis5.mattermost.model.Channel(
                channelCreateRequestDto.getDisplayName(),
                channelCreateRequestDto.getName().toLowerCase(),
                net.bis5.mattermost.model.ChannelType.of(channelCreateRequestDto.getType().getCode()),
                channelCreateRequestDto.getTeamId());

        Response response = client.createChannel(channel).getRawResponse();
        JSONObject resObj = new JSONObject();
        try {
            resObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            log.info("response.getEntity() = {}", response.getEntity());
            e.printStackTrace();
        }
        String channelId = resObj.getString("id");

        Channel channelResult = channelRepository.save(Channel.builder()
                .id(resObj.getString("id"))
                .team(Team.builder().id(resObj.getString("team_id")).build())
                .type(ChannelType.of(resObj.getString("type")))
                .name(resObj.getString("name"))
                .displyName(resObj.getString("display_name"))
                .build());

        List<ChannelUser> channelUserList = new ArrayList<>();
        for (String inviteUserId : channelCreateRequestDto.getInviteList()) {
            client.addChannelMember(channelId, inviteUserId);
            User user = userRepository.findById(inviteUserId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
            ChannelUser channelUser = ChannelUser.builder()
                    .user(user)
                    .channel(channelResult)
                    .build();

            channelUserList.add(channelUser);

        }
        channelUserRepository.saveAll(channelUserList);

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        channelUserRepository.save(ChannelUser.builder().channel(channelResult).user(user).build());

    }
}
