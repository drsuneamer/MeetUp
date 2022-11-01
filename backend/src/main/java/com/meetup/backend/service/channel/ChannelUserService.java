package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingChannelDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;

import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/27
 */
public interface ChannelUserService {

    List<ChannelResponseDto> getChannelByUser(String userId, String teamId);

    List<ChannelUser> getChannelUserByUser(String userId);

    List<UserInfoDto> getMeetupUserByChannel(Channel channel);

    void registerChannelUserFromMattermost(String mmSessionToken, List<Channel> channelList);

    List<MeetingChannelDto> getMeetingChannelByUsers(String userId, String managerId);

}
