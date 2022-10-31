package com.meetup.backend.dto.meetup;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
