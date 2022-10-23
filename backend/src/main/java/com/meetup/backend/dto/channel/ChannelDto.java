package com.meetup.backend.dto.channel;

import com.meetup.backend.entity.channel.Channel;
import lombok.Builder;
import lombok.Data;

/**
 * created by seungyong on 2022/10/22
 * updated by seungyong on 2022/10/22
 */
@Data
@Builder
public class ChannelDto {

    private String id;
    private String name;
    private String displayName;

    public static ChannelDto of(Channel channel){
        return ChannelDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .displayName(channel.getDisplayName())
                .build();
    }

}
