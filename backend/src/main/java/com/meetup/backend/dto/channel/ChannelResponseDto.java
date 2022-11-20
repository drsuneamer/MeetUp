package com.meetup.backend.dto.channel;

import com.meetup.backend.entity.channel.Channel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "채널 아이디")
    private String id;

    @ApiModelProperty(example = "보여지는 채널 이름")
    private String displayName;

    @ApiModelProperty(example = "채널이 속한 팀 이름")
    private String TeamId;

    public static ChannelResponseDto of(Channel channel) {
        return ChannelResponseDto.builder()
                .id(channel.getId())
                .displayName(channel.getDisplayName())
                .TeamId(channel.getTeam().getId())
                .build();
    }

}
