package com.meetup.backend.dto.Schedule.meeting;

import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by myeongseok on 2022/10/23
 * updated by myeongseok on 2022/10/25
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

    private String userName;

    private String userId;

    private Long meetupId;

    private String meetupName;

    private String meetupColor;

    private String meetupAdminUserId;

    private String meetupAdminUserName;

    public static MeetingResponseDto of(Schedule schedule, Meetup meetup, User user, User meetingUser) {
        return MeetingResponseDto.builder()
                .id(schedule.getId())
                .start(schedule.getStart())
                .end(schedule.getEnd())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .userId(user.getId())
                .userName(user.getNickname())
                .meetupId(meetup.getId())
                .meetupName(meetup.getTitle())
                .meetupColor(meetup.getColor())
                .meetupAdminUserId(meetingUser.getId())
                .meetupAdminUserName(meetingUser.getNickname())
                .build();
    }
}
