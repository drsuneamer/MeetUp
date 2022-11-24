package com.meetup.backend.dto.schedule.meeting;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * created by myeongseok on 2022/10/25
 * updated vy seongmin on 2022/11/09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeetingUpdateRequestDto {
    @NotNull
    private Long id;

    @NotBlank(message = "미팅의 시작 시간은 필수입니다.")
    private String start;

    @NotBlank(message = "미팅의 종료 시간은 필수입니다.")
    private String end;

    @NotBlank(message = "미팅의 제목은 필수입니다.")
    private String title;

    private String content;

    private boolean open;
}
