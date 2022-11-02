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
public class MeetupUpdateResponseDto {

    @ApiModelProperty(example = "Meetup 아이디 ")
    private Long id;

    @ApiModelProperty(example = "Meetup 이름")
    private String title;

    @ApiModelProperty(example = "Meetup이 속한 팀의 이름")
    private String teamName;

    @ApiModelProperty(example = "Meetup이 속한 채널 이름")
    private String channelName;

    @ApiModelProperty(example = "Meetup 색상")
    private String color;

    public static MeetupUpdateResponseDto of(Meetup meetup) {
        return MeetupUpdateResponseDto.builder()
                .id(meetup.getId())
                .title(meetup.getTitle())
                .color(meetup.getColor())
                .teamName(meetup.getChannel().getTeam().getDisplayName())
                .channelName(meetup.getChannel().getDisplayName())
                .build();
    }

}
