package com.meetup.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * created by myeongseok on 2022/10/24
 * updated by seongmin on 2022/11/06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class
ScheduleRequestDto {
    @NotBlank(message = "일정의 시작 시간은 필수입니다.")
    private String start;

    @NotBlank(message = "일정의 종료 시간은 필수입니다.")
    private String end;

    @NotBlank(message = "일정의 제목은 필수입니다.")
    private String title;

    private String content;

    private boolean open;

}
