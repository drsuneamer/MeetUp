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

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "밋업 색은 필수 입력 값입니다.")
    private String color;

}
