package com.meetup.backend.dto.meetup;

import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.dto.team.TeamResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * created by seungyong on 2022/10/27
 */
@Data
@Builder
public class TeamChannelResponseDto {

    private List<TeamResponseDto> teamList;
    private List<ChannelResponseDto> channelList;

}
