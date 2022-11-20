package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelCreateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.team.Team;
import org.json.JSONObject;

import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/23
 * updated by seungyong on 2022/10/27
 * updated by seungyong on 2022/10/29
 */
public interface ChannelService {

    List<Channel> registerChannelFromMattermost(String userId, String mmSessionToken, List<Team> teamList);

    void createNewChannel(String userId, String mmSessionToken, ChannelCreateRequestDto channelCreateRequestDto);

}
