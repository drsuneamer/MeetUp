package com.meetup.backend.dto.schedule;

import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by seongmin on 2022/10/31
 * updated by seongmin on 2022/11/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllScheduleResponseDto {
    // 1.  나 -> 타인 신청 미팅 목록
    private List<MeetingResponse> meetingFromMe;
    // 2. 타인 -> 나 신청 미팅 목록
    private List<MeetingResponse> meetingToMe;
    // 3. 나의 개인 스케쥴
    private List<ScheduleResponse> scheduleResponseList;
    // 4. 내가 속한 그룹의 일정
    private List<PartyMeetingResponse> partyMeetingResponseList;

    // owner가 me한테 신청한게 안보임
    public static AllScheduleResponseDto of(List<Schedule> scheduleList, List<Meeting> meetings, List<Meeting> partyMeetings, String me) {
        // 1.  나 -> 타인 신청 미팅 목록
        List<MeetingResponse> meetingFromMe = new ArrayList<>();
        // 2. 타인 -> 나 신청 미팅 목록
        List<MeetingResponse> meetingToMe = new ArrayList<>();
        // 3. 나의 개인 스케쥴
        List<ScheduleResponse> scheduleResponseList = new ArrayList<>();
        // 4. 내가 속한 그룹의 일정
        // 내가 그룹장 신청 미팅 = 나 -> 타인 신청 미팅  / 해당 경우 미팅을 목록에서 제거
        List<PartyMeetingResponse> partyMeetingResponseList = new ArrayList<>();

        // 4. 내가 속한 그룹의 일정
        for (Meeting meeting : partyMeetings) {
            partyMeetingResponseList.add(new PartyMeetingResponse(meeting.getId(), true, meeting.getStart(), meeting.getEnd(), meeting.getTitle(), meeting.getContent(), meeting.getUser().getId(), meeting.getUser().getNickname(), meeting.getMeetup().getTitle(), meeting.getMeetup().getColor(), meeting.getParty().getId(), meeting.getParty().getName()));
        }

        // 3 + 1
        for (Schedule schedule : scheduleList) {
            // 1.  나 -> 타인 신청 미팅 목록
            if (schedule instanceof Meeting) {
                // 스케줄 주인이 신청한 미팅 리스트
                Meeting meeting = (Meeting) schedule;
                // 만약 그룹에 속한 미팅에서 "내가 그룹장인 미팅"이 있다면 미팅 목록에서 제거
                for (PartyMeetingResponse partyMeetingResponse : partyMeetingResponseList) {
                    if (partyMeetingResponse.getId() != null && partyMeetingResponse.getId().equals(meeting.getId()))
                        continue;
                }
                // meeting id가 partyMeetingResponseList id에 있으면 continue;
                if (!schedule.isOpen() && !me.equals(schedule.getUser().getId()) && !me.equals(meeting.getMeetup().getManager().getId())) {
                    meetingFromMe.add(new MeetingResponse(schedule.getId(), false, schedule.getStart(), schedule.getEnd()));
                } else {
                    meetingFromMe.add(new MeetingResponse(schedule.getId(), true, schedule.getStart(), schedule.getEnd(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getId(), schedule.getUser().getNickname(), ((Meeting) schedule).getMeetup().getTitle(), ((Meeting) schedule).getMeetup().getColor()));
                }
            }
            // 3. 나의 개인 스케쥴
            else {
                if (!schedule.isOpen() && !me.equals(schedule.getUser().getId())) {
                    scheduleResponseList.add(new ScheduleResponse(schedule.getId(), false, schedule.getStart(), schedule.getEnd()));
                } else {
                    scheduleResponseList.add(new ScheduleResponse(schedule.getId(), true, schedule.getStart(), schedule.getEnd(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getId(), schedule.getUser().getNickname()));
                }
            }
        }
        // 2. 타인 -> 나 신청 미팅 목록
        for (Meeting meeting : meetings) {
            if (!meeting.isOpen() && !me.equals(meeting.getMeetup().getManager().getId()) && !me.equals(meeting.getUser().getId()) && !meeting.getParty().getPartyUsers().stream().map(partyUser -> partyUser.getUser().getId()).collect(Collectors.toList()).contains(me)) {
                meetingToMe.add(new MeetingResponse(meeting.getId(), false, meeting.getStart(), meeting.getEnd()));
            } else {
                meetingToMe.add(new MeetingResponse(meeting.getId(), true, meeting.getStart(), meeting.getEnd(), meeting.getTitle(), meeting.getContent(), meeting.getUser().getId(), meeting.getUser().getNickname(), meeting.getMeetup().getTitle(), meeting.getMeetup().getColor()));
            }
        }
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

        @Override
        public String toString() {
            return "PartyMeetingResponse{" + "partyId=" + partyId + ", partyName='" + partyName + '\'' + super.toString();
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
            return "MeetingResponse{" + "meetupName='" + meetupName + '\'' + ", meetupColor='" + meetupColor + '\'' + super.toString();
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
            return "ScheduleResponse{" + "id=" + id + ", open=" + open + ", start=" + start + ", end=" + end + ", title='" + title + '\'' + ", content='" + content + '\'' + ", userId='" + userId + '\'' + ", userName='" + userName + '\'' + '}';
        }
    }

    // 등록 가능 체크
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

    // 수정 가능 체크
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

    // 중복 체크
    public boolean isDuplicated(LocalDateTime start, LocalDateTime end, LocalDateTime from, LocalDateTime to) {
        if (from.isBefore(end) && to.isAfter(end))
            return true;
        else if (from.isBefore(start) && to.isAfter(start))
            return true;
        else if ((from.isBefore(start) || from.isEqual(start)) && (to.isAfter(end) || to.isEqual(end)))
            return true;
        else if ((from.isAfter(start) || from.isEqual(start)) && (to.isBefore(end) || to.isEqual(end)))
            return true;
        else return false;
    }

}
