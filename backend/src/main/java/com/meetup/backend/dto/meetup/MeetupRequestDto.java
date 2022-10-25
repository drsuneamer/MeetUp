package com.meetup.backend.dto.meetup;

import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * created by seungyong on 2022/10/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetupRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String color;

    @NotBlank
    private String channelId;

}
