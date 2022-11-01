package com.meetup.backend.dto.schedule.meeting;

import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * created by seungyong on 2022/11/01
 */
@Data
@Builder
public class MeetingChannelDto {

    @ApiModelProperty(example = "밋업 아이디")
    private Long meetupId;

    @ApiModelProperty(example = "보여지는 채널 이름")
    private String displayName;

    public static MeetingChannelDto of(Meetup meetup, Channel channel) {
        return MeetingChannelDto.builder()
                .meetupId(meetup.getId())
                .displayName(channel.getDisplayName())
                .build();
    }

}
