package com.meetup.backend.service.channel;

import org.json.JSONArray;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/23
 * updated by seungyong on 2022/10/27
 */
public interface ChannelService {

    JSONArray registerChannelFromMattermost(String userId, String teamId, String mmSessionToken);

}
