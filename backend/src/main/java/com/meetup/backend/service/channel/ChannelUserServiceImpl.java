package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.user.UserService;
import com.meetup.backend.util.converter.JsonConverter;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.client4.Pager;
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
 * updated by seungyong on 2022/10/27
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChannelUserServiceImpl implements ChannelUserService {

    @Autowired
    private final ChannelUserRepository channelUserRepository;

    @Autowired
    private final ChannelRepository channelRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Override
    public List<ChannelResponseDto> getChannelByUser(String userId, String teamId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        List<ChannelResponseDto> channelResponseDtoList = new ArrayList<>();

        for (ChannelUser channelUser : channelUserRepository.findByUser(user)) {
            channelResponseDtoList.add(ChannelResponseDto.of(channelUser.getChannel()));
        }

        return channelResponseDtoList;
    }

    @Override
    public void registerChannelUserFromMattermost(String mmSessionToken, JSONArray channelArray) {

        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        for (int i = 0; i < channelArray.length(); i++) {

            JSONObject channelObj = channelArray.getJSONObject(i);
            if (channelObj.getString("type").equals("D") || channelObj.getString("type").equals("G")) continue;
            Channel channel = channelRepository.findById(channelObj.getString("id")).orElseThrow(() -> new ApiException(ExceptionEnum.CHANNEL_NOTFOUND));

            List<User> userList = new ArrayList<>();

            for (int k = 0; ; k++) {

                Response mmChannelUserResponse = client.getChannelMembers(channel.getId(), Pager.of(k, 100)).getRawResponse();
                JSONArray userArray = JsonConverter.toJsonArray((BufferedInputStream) mmChannelUserResponse.getEntity());
                if (userArray.isEmpty()) break;

                for (int l = 0; l < userArray.length(); l++) {

                    userList.add(User.builder().id(userArray.getJSONObject(l).getString("user_id")).build());
                }

            }

            userService.registerUserFromList(userList);

            for (User user : userList) {
                if (channelUserRepository.findByChannelAndUser(channel, user).isEmpty()) {
                    ChannelUser channelUser = ChannelUser.builder().channel(channel).user(user).build();
                    channelUserRepository.save(channelUser);
                }
            }

        }

    }
}
