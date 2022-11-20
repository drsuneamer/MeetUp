package com.meetup.backend.dto.schedule.meeting;

import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.party.Party;
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

    private String userWebex;

    private Long meetupId;

    private String meetupName;

    private String meetupColor;

    private String meetupAdminUserId;

    private String meetupAdminUserName;

    private String meetupAdminUserWebex;

    private Long partyId;

    private String partyName;

    public static MeetingResponseDto of(Schedule schedule, Meetup meetup, User user, User meetingUser, Party party) {
        if (party != null)
            return MeetingResponseDto.builder()
                    .id(schedule.getId())
                    .start(schedule.getStart())
                    .end(schedule.getEnd())
                    .title(schedule.getTitle())
                    .content(schedule.getContent())
                    .userId(user.getId())
                    .userName(user.getNickname())
                    .userWebex(user.getWebex())
                    .meetupId(meetup.getId())
                    .meetupName(meetup.getTitle())
                    .meetupColor(meetup.getColor())
                    .meetupAdminUserId(meetingUser.getId())
                    .meetupAdminUserName(meetingUser.getNickname())
                    .meetupAdminUserWebex(meetingUser.getWebex())
                    .partyId(party.getId())
                    .partyName(party.getName())
                    .build();
        else
            return MeetingResponseDto.builder()
                    .id(schedule.getId())
                    .start(schedule.getStart())
                    .end(schedule.getEnd())
                    .title(schedule.getTitle())
                    .content(schedule.getContent())
                    .userId(user.getId())
                    .userName(user.getNickname())
                    .userWebex(user.getWebex())
                    .meetupId(meetup.getId())
                    .meetupName(meetup.getTitle())
                    .meetupColor(meetup.getColor())
                    .meetupAdminUserId(meetingUser.getId())
                    .meetupAdminUserName(meetingUser.getNickname())
                    .meetupAdminUserWebex(meetingUser.getWebex())
                    .partyId(null)
                    .partyName(null)
                    .build();
    }
}
