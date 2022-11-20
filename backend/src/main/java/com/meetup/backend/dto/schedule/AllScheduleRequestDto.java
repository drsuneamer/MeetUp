package com.meetup.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * created by seongmin on 2022/10/31
 * updated by seongmin on 2022/11/01
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllScheduleRequestDto {

    @NotNull(message = "스케줄을 확인 할 아이디는 필수 입력값입니다.")
    private String targetId;
    @NotEmpty(message = "날짜는 필수 입력값입니다.")
    private String date;
}
