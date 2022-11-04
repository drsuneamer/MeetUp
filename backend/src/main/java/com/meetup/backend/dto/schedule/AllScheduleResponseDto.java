package com.meetup.backend.dto.schedule;

import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * created by seongmin on 2022/10/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllScheduleResponseDto {


    private List<MeetingResponse> meetingFromMe; // 내가 신청한 미팅
    private List<MeetingResponse> meetingToMe; // 내가 신청받은 미팅
    private List<ScheduleResponse> scheduleResponseList;

    public static AllScheduleResponseDto of(List<Schedule> scheduleList, List<Meeting> meetings) {
        List<MeetingResponse> meetingFromMe = new ArrayList<>();
        List<MeetingResponse> meetingToMe = new ArrayList<>();
        List<ScheduleResponse> scheduleResponseList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            if (schedule instanceof Meeting) {
                meetingFromMe.add(new MeetingResponse(
                        schedule.getId(),
                        schedule.getStart(),
                        schedule.getEnd(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getUser().getId(),
                        schedule.getUser().getNickname(),
                        ((Meeting) schedule).getMeetup().getTitle(),
                        ((Meeting) schedule).getMeetup().getColor()
                ));
            } else {
                scheduleResponseList.add(new ScheduleResponse(
                        schedule.getId(),
                        schedule.getStart(),
                        schedule.getEnd(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getUser().getId(),
                        schedule.getUser().getNickname()
                ));
            }
        }
        for (Meeting meeting : meetings) {
            meetingToMe.add(new MeetingResponse(
                    meeting.getId(),
                    meeting.getStart(),
                    meeting.getEnd(),
                    meeting.getTitle(),
                    meeting.getContent(),
                    meeting.getUser().getId(),
                    meeting.getUser().getNickname(),
                    meeting.getMeetup().getTitle(),
                    meeting.getMeetup().getColor()
            ));
        }
        return new AllScheduleResponseDto(meetingFromMe, meetingToMe, scheduleResponseList);
    }

    @Getter
    private static class MeetingResponse extends ScheduleResponse {
        private String meetupName;

        private String meetupColor;

        public MeetingResponse(Long id, LocalDateTime start, LocalDateTime end, String title, String content, String userId, String userName, String meetupName, String meetupColor) {
            super(id, start, end, title, content, userId, userName);
            this.meetupName = meetupName;
            this.meetupColor = meetupColor;
        }
    }

    @Getter
    private static class ScheduleResponse {
        private Long id;

        private LocalDateTime start;

        private LocalDateTime end;

        private String title;

        private String content;

        private String userId;

        private String userName;

        public ScheduleResponse(Long id, LocalDateTime start, LocalDateTime end, String title, String content, String userId, String userName) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.title = title;
            this.content = content;
            this.userId = userId;
            this.userName = userName;
        }
    }

    public boolean isPossibleRegiser(LocalDateTime from, LocalDateTime to) {
        for (MeetingResponse meetingResponse : meetingFromMe) {
            if (isDuplicated(meetingResponse.getStart(), meetingResponse.getEnd(), from, to))
                return false;
        }
        for (MeetingResponse meetingResponse : meetingToMe) {
            if (isDuplicated(meetingResponse.getStart(), meetingResponse.getEnd(), from, to))
                return false;
        }
        for (ScheduleResponse ScheduleResponse : scheduleResponseList) {
            if (isDuplicated(ScheduleResponse.getStart(), ScheduleResponse.getEnd(), from, to))
                return false;
        }
        return true;
    }

    public boolean isDuplicated(LocalDateTime start, LocalDateTime end, LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(start) && from.isBefore(end))
            return true;
        else if (to.isAfter(start) && to.isBefore(end))
            return true;
        else if ((from.isBefore(start) || from.isEqual(start)) && (to.isAfter(end) || to.isEqual(end)))
            return true;
        else if (from.isAfter(start) && to.isBefore(end))
            return true;
        else
            return false;

    }
}
