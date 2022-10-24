package com.meetup.backend.dto.meeting;

import com.meetup.backend.entity.meeting.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by myeongseok on 2022/10/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingResponseDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private String title;

    private String content;

    private String managerId;

    private String applicantId;

    private Long meetupId;

    public static MeetingResponseDto of(Meeting meeting) {
        return MeetingResponseDto.builder()
                .id(meeting.getId())
                .start(meeting.getStart())
                .end(meeting.getEnd())
                .title(meeting.getTitle())
                .content(meeting.getContent())
                .managerId(meeting.getManager().getId())
                .applicantId(meeting.getApplicant().getId())
                .meetupId(meeting.getMeetup().getId())
                .build();
    }
}
