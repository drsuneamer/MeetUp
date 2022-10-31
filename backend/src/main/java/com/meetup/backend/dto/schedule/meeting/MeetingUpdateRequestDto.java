package com.meetup.backend.dto.schedule.meeting;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MeetingUpdateRequestDto {
    @NotNull
    private Long Id;

    @NotBlank(message = "미팅의 시작 시간은 필수입니다.")
    private String start;

    @NotBlank(message = "미팅의 종료 시간은 필수입니다.")
    private String end;

    @NotBlank(message = "미팅의 제목은 필수입니다.")
    private String title;

    private String content;

    @NotBlank(message = "미팅의 managerId는 필수 입니다.")
    private String managerId;

    @NotBlank(message = "미팅의 meetupId는 필수 입니다.")
    private Long meetupId;
}
