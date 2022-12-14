package com.meetup.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * created by myeongseok on 2022/10/27
 * updated by myeongseok on 2022/11/06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleUpdateRequestDto {

    @NotNull
    private Long id;

    @NotBlank(message = "일정의 시작 시간은 필수입니다.")
    private String start;

    @NotBlank(message = "일정의 종료 시간은 필수입니다.")
    private String end;

    @NotBlank(message = "일정의 제목은 필수입니다.")
    private String title;

    private String content;

    private boolean open;


}
