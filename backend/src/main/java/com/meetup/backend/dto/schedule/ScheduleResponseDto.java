package com.meetup.backend.dto.schedule;

import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.schedule.ScheduleType;
import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by myeongseok on 2022/10/25
 * updated by seongmin on 2022/11/08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long id;

    private ScheduleType type;

    private LocalDateTime start;

    private LocalDateTime end;

    private String title;

    private String content;

    private boolean open;

    private String managerId;

    private String managerName;

    private String userId;

    private String userName;

    private String myWebex;

    private String diffWebex;

    private Long meetupId;

    private Long partyId;

    private String partyName;

    public static ScheduleResponseDto of(Schedule schedule, User user) {
        ScheduleResponseDto result = ScheduleResponseDto.builder()
                .id(schedule.getId())
                .type(schedule.getType())
                .start(schedule.getStart())
                .end(schedule.getEnd())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .open(schedule.isOpen())
                .userId(schedule.getUser().getId())
                .userName(schedule.getUser().getNickname())
                .myWebex(user.getWebex())
                .diffWebex(schedule.getUser().getWebex())
                .build();

        if (schedule.getType().equals(ScheduleType.Meeting)) {
            Meeting meeting = (Meeting) schedule;
            result.setManagerId(meeting.getMeetup().getManager().getId());
            result.setManagerName(meeting.getMeetup().getManager().getNickname());
            result.setDiffWebex(meeting.getMeetup().getManager().getWebex());
            result.setMeetupId(meeting.getMeetup().getId());
            if (meeting.getParty() != null) {
                result.setPartyId(meeting.getParty().getId());
                result.setPartyName(meeting.getParty().getName());
            } else {
                result.setPartyId(null);
                result.setPartyName(null);
            }
        }
        return result;

    }
}

