package com.meetup.backend.dto.channel;

import com.meetup.backend.entity.channel.Channel;
import lombok.Builder;
import lombok.Data;

/**
 * created by seungyong on 2022/10/22
 * updated by seungyong on 2022/10/22
 * updated by seungyong on 2022/10/27
 */
@Data
@Builder
public class ChannelResponseDto {

    private String id;
    private String displayName;
    private String parentId;

    public static ChannelResponseDto of(Channel channel) {
        return ChannelResponseDto.builder()
                .id(channel.getId())
                .displayName(channel.getDisplayName())
                .parentId(channel.getTeam().getId())
                .build();
    }

}
