package com.meetup.backend.dto.schedule.meeting;

import com.meetup.backend.entity.channel.Channel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * created by seungyong on 2022/11/01
 */
@Data
@Builder
public class MeetingChannelDto {

    @ApiModelProperty(example = "채널 아이디")
    private String id;

    @ApiModelProperty(example = "보여지는 채널 이름")
    private String displayName;

    public static MeetingChannelDto of(Channel channel) {
        return MeetingChannelDto.builder()
                .id(channel.getId())
                .displayName(channel.getDisplayName())
                .build();
    }

}
