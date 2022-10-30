package com.meetup.backend.service.channel;

import com.meetup.backend.entity.team.Team;
import org.json.JSONArray;

import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/23
 * updated by seungyong on 2022/10/27
 * updated by seungyong on 2022/10/29
 */
public interface ChannelService {

    JSONArray registerChannelFromMattermost(String userId, String mmSessionToken, List<Team> teamList);

}
