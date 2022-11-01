package com.meetup.backend.dto.meetup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * created by seungyong on 2022/11/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetupUpdateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String color;

}
