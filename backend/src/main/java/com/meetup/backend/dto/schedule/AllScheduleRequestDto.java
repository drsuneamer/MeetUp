package com.meetup.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * created by seongmin on 2022/10/31
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllScheduleRequestDto {

    @NotNull(message = "밋업 아이디는 필수 입력값입니다.")
    private Long meetupId;
    @NotEmpty(message = "날짜는 필수 입력값입니다.")
    private String date;
}
