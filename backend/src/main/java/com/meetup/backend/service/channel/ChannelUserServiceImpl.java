package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingChannelDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.exception.MattermostEx;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.client4.Pager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by myeongseok on 2022/10/21
 * updated by seongmin on 2022/11/04
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChannelUserServiceImpl implements ChannelUserService {

    private final ChannelUserRepository channelUserRepository;

    private final ChannelRepository channelRepository;

    private final TeamUserRepository teamUserRepository;

    private final UserRepository userRepository;

    private final MeetupRepository meetupRepository;

    private final AuthService authService;

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
                try {
                    JSONObject jsonObject = JsonConverter.toJson((BufferedInputStream) userResponse.getEntity());
                    nickname = (String) jsonObject.get("nickname");
                } catch (ClassCastException e) {
                    log.error(e.getMessage());
                    log.info("userResponse.getEntity() = {}", userResponse.getEntity());
                    e.printStackTrace();
                }
            }
            UserInfoDto userInfoDto = UserInfoDto.of(channelUser.getUser().getId(), nickname);
            userInfoDtoList.add(userInfoDto);
        }

        return userInfoDtoList;
    }

    // db에 저장되어 있지 않은 채널만 ChannelUser db 저장
    // ChannelUser 이미 저장되어 있는지 확인 할 필요 없음
    @Override
    public void registerChannelUserFromMattermost(String mmSessionToken, List<Channel> channelList) {

        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        for (Channel channel : channelList) {
            List<User> userList = new ArrayList<>();
            for (int k = 0; ; k++) {

                Response mmChannelUserResponse = client.getChannelMembers(channel.getId(), Pager.of(k, 100)).getRawResponse();

                MattermostEx.apiException(mmChannelUserResponse.getStatus());

                JSONArray userArray = new JSONArray();
                try {
                    userArray = JsonConverter.toJsonArray((BufferedInputStream) mmChannelUserResponse.getEntity());

                } catch (ClassCastException e) {
                    log.error(e.getMessage());
                    log.info("mmChannelUserResponse.getEntity() = {}", mmChannelUserResponse.getEntity());
                    e.printStackTrace();
                }
                if (userArray.isEmpty()) break;

                for (int l = 0; l < userArray.length(); l++) {

                    String userId = userArray.getJSONObject(l).getString("user_id");
//                    User user = userRepository.findById(userId).orElseGet(
//                            () -> userRepository.save(
//                                    User.builder()
//                                            .id(userId)
//                                            .firstLogin(false)
//                                            .role(RoleType.Student)
//                                            .build()
//                            )
//                    );
                    userList.add(userRepository.findById(userId).orElse(User.builder()
                            .id(userId)
                            .firstLogin(false)
                            .role(RoleType.ROLE_Student)
                            .build()));
//                    if (channelUserRepository.findByChannelAndUser(channel, user).isEmpty()) {
//                        ChannelUser channelUser = ChannelUser.builder().channel(channel).user(user).build();
//                        channelUserRepository.save(channelUser);
//                    }
                }

            }
            Set<ChannelUser> channelUserSet = new HashSet<>(channelUserRepository.findByChannel(channel));


//            channelUserRepository.saveAll(userList.stream().filter(user -> !channelUserRepository.existsByChannelAndUser(channel, user))
//                    .map(user -> ChannelUser.builder().channel(channel).user(user).build())
//                    .collect(Collectors.toList()));
            channelUserRepository.saveAll(userList.stream().map(user -> ChannelUser.builder().channel(channel).user(user).build())
                    .filter(channelUser -> !channelUserSet.contains(channelUser))
                    .collect(Collectors.toList()));
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

    @Override
    public List<ChannelResponseDto> getActivatedChannelByUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        List<ChannelResponseDto> channelResponseDtoList = new ArrayList<>();
        List<Team> teamList = new ArrayList<>();

        for (TeamUser teamUser : teamUserRepository.findByUser(user)) {
            if (!teamUser.isActivate())
                continue;
            teamList.add(teamUser.getTeam());
        }

        for (Team team : teamList) {
            for (Channel channel : channelRepository.findByTeam(team)) {
                channelResponseDtoList.add(ChannelResponseDto.of(channel));
            }
        }

        return channelResponseDtoList;
    }

}
