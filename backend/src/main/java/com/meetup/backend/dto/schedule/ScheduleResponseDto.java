package com.meetup.backend.dto.schedule;

import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by myeongseok on 2022/10/25
 * updated by seongmin on 2022/11/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private String title;

    private String content;

    private String userId;

    private String userName;

    private String myWebex;

    private String diffWebex;

    public static ScheduleResponseDto of(Schedule schedule, User user) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .start(schedule.getStart())
                .end(schedule.getEnd())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .userId(schedule.getUser().getId())
                .userName(schedule.getUser().getNickname())
                .myWebex(user.getWebex())
                .diffWebex(schedule.getUser().getWebex())
                .build();
    }
}

