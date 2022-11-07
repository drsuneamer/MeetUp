package com.meetup.backend.dto.meetup;

import com.meetup.backend.entity.meetup.Meetup;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetupResponseDto {
    @ApiModelProperty(example = "Meetup 아이디 ")
    private Long id;

    @ApiModelProperty(example = "Meetup 이름")
    private String title;

    @ApiModelProperty(example = "Meetup 색상")
    private String color;

    @ApiModelProperty(example = "기존 채널 이름")
    private String channelName;

    public static MeetupResponseDto of(Meetup meetup) {
        return MeetupResponseDto.builder()
                .id(meetup.getId())
                .title(meetup.getTitle())
                .color(meetup.getColor())
                .channelName(meetup.getChannel().getDisplayName())
                .build();
    }

}
