package com.meetup.backend.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * created by myeongseok on 2022/10/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequestDto {
    @NotBlank
    private String start;

    @NotBlank
    private String end;

    @NotBlank
    private String title;

    private String content;

    @NotBlank(message = "managerId는 필수 입니다.")
    private String managerId;

    @NotBlank(message = "applicantId는 필수 입니다.")
    private String applicantId;

    @NotBlank(message = "meetupId는 필수 입니다.")
    private Long meetupId;

    private List<String> list

}
