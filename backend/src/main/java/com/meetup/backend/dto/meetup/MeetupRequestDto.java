package com.meetup.backend.dto.meetup;

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
    private String teamName;

    @NotBlank
    private String channelName;

    @NotBlank
    private String meetupColor;

}
