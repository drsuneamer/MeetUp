package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingChannelDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
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

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by myeongseok on 2022/10/21
 * updated by seongmin on 2022/11/01
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChannelUserServiceImpl implements ChannelUserService {

    private final ChannelUserRepository channelUserRepository;

    private final UserRepository userRepository;

    private final MeetupRepository meetupRepository;

    private final AuthService authService;

    @Override
    public List<ChannelResponseDto> getChannelByUser(String userId, String teamId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        List<ChannelResponseDto> channelResponseDtoList = new ArrayList<>();

        for (ChannelUser channelUser : channelUserRepository.findByUser(user)) {
            channelResponseDtoList.add(ChannelResponseDto.of(channelUser.getChannel()));
        }

        return channelResponseDtoList;
    }

    @Override
    public List<ChannelUser> getChannelUserByUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        return channelUserRepository.findByUser(user);
    }

    @Override
    public List<UserInfoDto> getMeetupUserByChannel(Channel channel, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        if (!channelUserRepository.existsByChannelAndUser(channel, user)) {
            throw new ApiException(CHANNEL_ACCESS_DENIED);
        }
        List<ChannelUser> channelUserList = channelUserRepository.findByChannel(channel);
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        MattermostClient client = Client.getClient();
        client.setAccessToken(authService.getMMSessionToken(userId));
        for (ChannelUser channelUser : channelUserList) {
            String nickname = channelUser.getUser().getNickname();
            if (nickname == null) {
                Response userResponse = client.getUser(channelUser.getUser().getId()).getRawResponse();
                JSONObject jsonObject = JsonConverter.toJson((BufferedInputStream) userResponse.getEntity());
                nickname = (String) jsonObject.get("nickname");
            }
            UserInfoDto userInfoDto = UserInfoDto.of(channelUser.getUser().getId(), nickname);
            userInfoDtoList.add(userInfoDto);
        }

        return userInfoDtoList;
    }

    // db에 저장되어 있지 않은 팀만 ChannelUser db 저장
    @Override
    public void registerChannelUserFromMattermost(String mmSessionToken, List<Channel> channelList) {

        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        for (Channel channel : channelList) {

            for (int k = 0; ; k++) {

                Response mmChannelUserResponse = client.getChannelMembers(channel.getId(), Pager.of(k, 100)).getRawResponse();
                JSONArray userArray = JsonConverter.toJsonArray((BufferedInputStream) mmChannelUserResponse.getEntity());
                if (userArray.isEmpty()) break;

                for (int l = 0; l < userArray.length(); l++) {

                    String userId = userArray.getJSONObject(l).getString("user_id");
                    User user = userRepository.findById(userId).orElseGet(
                            () -> userRepository.save(
                                    User.builder()
                                            .id(userId)
                                            .firstLogin(false)
                                            .role(RoleType.Student)
                                            .build()
                            )
                    );
                    if (channelUserRepository.findByChannelAndUser(channel, user).isEmpty()) {
                        ChannelUser channelUser = ChannelUser.builder().channel(channel).user(user).build();
                        channelUserRepository.save(channelUser);
                    }
                }

            }

        }

    }

    @Override
    public List<MeetingChannelDto> getMeetingChannelByUsers(String userId, String managerId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        User manager = userRepository.findById(managerId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        List<MeetingChannelDto> channelList = new ArrayList<>();

        for (Meetup meetup : meetupRepository.findByManager(manager)) {
            if (meetup.isDelete())
                continue;
            Channel channel = meetup.getChannel();
            if (channelUserRepository.existsByChannelAndUser(channel, user)) {
                channelList.add(MeetingChannelDto.of(meetup, channel));
            }

        }

        return channelList;
    }

}
