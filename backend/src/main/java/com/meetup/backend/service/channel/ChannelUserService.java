package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelResponseDto;

import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/27
 */
public interface ChannelUserService {

    List<ChannelResponseDto> getChannelByTeam(String userId);

}
