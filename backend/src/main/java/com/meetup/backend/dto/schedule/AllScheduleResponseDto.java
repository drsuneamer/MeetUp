package com.meetup.backend.dto.schedule;

import com.meetup.backend.entity.party.PartyMeeting;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * created by seongmin on 2022/10/31
 * updated by seongmin on 2022/11/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllScheduleResponseDto {
    private List<MeetingResponse> meetingFromMe; // 내가 신청한 미팅
    private List<MeetingResponse> meetingToMe; // 내가 신청받은 미팅
    private List<ScheduleResponse> scheduleResponseList;

    private List<PartyMeetingResponse> partyMeetingResponseList;

    // oner가 me한테 신청한게 안보임
    public static AllScheduleResponseDto of(List<Schedule> scheduleList, List<Meeting> meetings, List<PartyMeeting> partyMeetings, String me) {
        List<MeetingResponse> meetingFromMe = new ArrayList<>();
        List<MeetingResponse> meetingToMe = new ArrayList<>();
        List<ScheduleResponse> scheduleResponseList = new ArrayList<>();
        List<PartyMeetingResponse> partyMeetingResponseList = new ArrayList<>();

        for (PartyMeeting partyMeeting : partyMeetings) {
            partyMeetingResponseList.add(new PartyMeetingResponse(partyMeeting.getMeeting().getId(),
                    partyMeeting.getMeeting().isOpen(),
                    partyMeeting.getMeeting().getStart(),
                    partyMeeting.getMeeting().getEnd(),
                    partyMeeting.getMeeting().getTitle(),
                    partyMeeting.getMeeting().getContent(),
                    partyMeeting.getMeeting().getUser().getId(),
                    partyMeeting.getMeeting().getUser().getNickname(),
                    partyMeeting.getMeeting().getMeetup().getTitle(),
                    partyMeeting.getMeeting().getMeetup().getColor(),
                    partyMeeting.getParty().getId(),
                    partyMeeting.getParty().getName()));
        }


        for (Schedule schedule : scheduleList) {
            if (schedule instanceof Meeting) {
                // 스케줄 주인이 신청한 미팅 리스트
                Meeting meeting = (Meeting) schedule;

                // 만약 그룹에 속한 미팅에서 "내가 그룹장인 미팅"이 있다면 미팅 목록에서 제거"
                for (PartyMeetingResponse partyMeetingResponse : partyMeetingResponseList) {
                    if (partyMeetingResponse.getId().equals(meeting.getId()))
                        continue;
                }

                //meeting id가 partyMeetingResponseList id에 있으면 continue;
                if (!schedule.isOpen() && !me.equals(schedule.getUser().getId()) && !me.equals(meeting.getMeetup().getManager().getId())) {
                    meetingFromMe.add(new MeetingResponse(
                            schedule.getId(),
                            false,
                            schedule.getStart(),
                            schedule.getEnd()
                    ));
                } else {
                    meetingFromMe.add(new MeetingResponse(
                            schedule.getId(),
                            true,
                            schedule.getStart(),
                            schedule.getEnd(),
                            schedule.getTitle(),
                            schedule.getContent(),
                            schedule.getUser().getId(),
                            schedule.getUser().getNickname(),
                            ((Meeting) schedule).getMeetup().getTitle(),
                            ((Meeting) schedule).getMeetup().getColor()
                    ));
                }
            } else { // 스케줄 주인의 개인 스케줄 리스트
                if (!schedule.isOpen() && !me.equals(schedule.getUser().getId())) {
                    scheduleResponseList.add(new ScheduleResponse(
                            schedule.getId(),
                            false,
                            schedule.getStart(),
                            schedule.getEnd()
                    ));
                } else {
                    scheduleResponseList.add(new ScheduleResponse(
                            schedule.getId(),
                            true,
                            schedule.getStart(),
                            schedule.getEnd(),
                            schedule.getTitle(),
                            schedule.getContent(),
                            schedule.getUser().getId(),
                            schedule.getUser().getNickname()
                    ));
                }
            }
        }
        for (Meeting meeting : meetings) { // 스케줄 주인이 신청 받은 미팅 리스트
            if (!meeting.isOpen() && !me.equals(meeting.getMeetup().getManager().getId()) && !me.equals(meeting.getUser().getId())) {
                meetingToMe.add(new MeetingResponse(
                        meeting.getId(),
                        false,
                        meeting.getStart(),
                        meeting.getEnd()
                ));
            } else {
                meetingToMe.add(new MeetingResponse(
                        meeting.getId(),
                        true,
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
        }

        // 스케쥴 주인이 속한 그룹의 미팅

        return new AllScheduleResponseDto(meetingFromMe, meetingToMe, scheduleResponseList, partyMeetingResponseList);
    }

    @Getter
    private static class PartyMeetingResponse extends MeetingResponse {
        private Long partyId;
        private String partyName;

        public PartyMeetingResponse(Long id, boolean open, LocalDateTime start, LocalDateTime end, String title, String content, String userId, String userName, String meetupName, String meetupColor, Long partyId, String partyName) {
            super(id, open, start, end, title, content, userId, userName, meetupName, meetupColor);
            this.partyId = partyId;
            this.partyName = partyName;
        }
    }

    @Getter
    private static class MeetingResponse extends ScheduleResponse {
        private String meetupName;

        private String meetupColor;

        public MeetingResponse(Long id, boolean open, LocalDateTime start, LocalDateTime end, String title, String content, String userId, String userName, String meetupName, String meetupColor) {
            super(id, open, start, end, title, content, userId, userName);
            this.meetupName = meetupName;
            this.meetupColor = meetupColor;
        }

        public MeetingResponse(Long id, boolean open, LocalDateTime start, LocalDateTime end) {
            super(id, open, start, end);
        }

        @Override
        public String toString() {
            return "MeetingResponse{" +
                    "meetupName='" + meetupName + '\'' +
                    ", meetupColor='" + meetupColor + '\'' +
                    super.toString();
        }
    }

    @Getter
    private static class ScheduleResponse {
        private Long id;

        private boolean open;

        private LocalDateTime start;

        private LocalDateTime end;

        private String title;

        private String content;

        private String userId;

        private String userName;

        public ScheduleResponse(Long id, boolean open, LocalDateTime start, LocalDateTime end, String title, String content, String userId, String userName) {
            this.id = id;
            this.open = open;
            this.start = start;
            this.end = end;
            this.title = title;
            this.content = content;
            this.userId = userId;
            this.userName = userName;
        }

        public ScheduleResponse(Long id, boolean open, LocalDateTime start, LocalDateTime end) {
            this.id = id;
            this.open = open;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "ScheduleResponse{" +
                    "id=" + id +
                    ", open=" + open +
                    ", start=" + start +
                    ", end=" + end +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }

    public boolean isPossibleRegister(LocalDateTime from, LocalDateTime to) {
        for (MeetingResponse meetingResponse : meetingFromMe) {
            if (isDuplicated(meetingResponse.getStart(), meetingResponse.getEnd(), from, to)) {
                return false;
            }
        }
        for (MeetingResponse meetingResponse : meetingToMe) {
            if (isDuplicated(meetingResponse.getStart(), meetingResponse.getEnd(), from, to)) {
                return false;
            }
        }
        for (ScheduleResponse ScheduleResponse : scheduleResponseList) {
            if (isDuplicated(ScheduleResponse.getStart(), ScheduleResponse.getEnd(), from, to)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPossibleRegister(LocalDateTime from, LocalDateTime to, Long updateId) {
        for (MeetingResponse meetingResponse : meetingFromMe) {
            if (meetingResponse.getId().equals(updateId)) {
                continue;
            }
            if (isDuplicated(meetingResponse.getStart(), meetingResponse.getEnd(), from, to)) {
                return false;

            }
        }
        for (MeetingResponse meetingResponse : meetingToMe) {
            if (meetingResponse.getId().equals(updateId)) {
                continue;
            }
            if (isDuplicated(meetingResponse.getStart(), meetingResponse.getEnd(), from, to)) {
                return false;
            }
        }
        for (ScheduleResponse ScheduleResponse : scheduleResponseList) {
            if (ScheduleResponse.getId().equals(updateId)) {
                continue;
            }
            if (isDuplicated(ScheduleResponse.getStart(), ScheduleResponse.getEnd(), from, to)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDuplicated(LocalDateTime start, LocalDateTime end, LocalDateTime from, LocalDateTime to) {
        if (from.isBefore(end) && to.isAfter(end))
            return true;
        else if (from.isBefore(start) && to.isAfter(start))
            return true;
        else if ((from.isBefore(start) || from.isEqual(start)) && (to.isAfter(end) || to.isEqual(end)))
            return true;
        else if ((from.isAfter(start) || from.isEqual(start)) && (to.isBefore(end) || to.isEqual(end)))
            return true;
        else
            return false;

    }

}
