package com.meetup.backend.docs.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.backend.controller.ScheduleController;
import com.meetup.backend.dto.schedule.AllScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleRequestDto;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.schedule.ScheduleUpdateRequestDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.schedule.ScheduleType;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.meeting.ScheduleService;
import com.meetup.backend.util.converter.LocalDateUtil;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.meetup.backend.entity.user.RoleType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private AuthService authService;

    @Test
    public void getSchedule() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        ScheduleResponseDto scheduleResponseDto = ScheduleResponseDto.builder()
                .id(1L)
                .open(true)
                .partyId(2L)
                .partyName("2팀")
                .title("내 스케줄")
                .content("점심 약속")
                .start(LocalDateUtil.strToLDT("2022-11-16 12:00:00"))
                .end(LocalDateUtil.strToLDT("2022-11-16 13:00:00"))
                .meetupId(3L)
                .managerId("managerId")
                .managerName("manager")
                .myWebex("www.myWebex.com")
                .diffWebex("www.diffWebex.com")
                .type(ScheduleType.Schedule)
                .userId("userId")
                .userName("hong")
                .build();

        given(scheduleService.getScheduleDetail(anyString(), anyLong()))
                .willReturn(scheduleResponseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/schedule/{scheduleId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("schedule(meeting)-detail-by-scheduleId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("스케줄 아이디"),
                                fieldWithPath("open").type(JsonFieldType.BOOLEAN).description("스케줄 공개 여부"),
                                fieldWithPath("partyId").type(JsonFieldType.NUMBER).description("그룹 아이디"),
                                fieldWithPath("partyName").type(JsonFieldType.STRING).description("그룹 이름"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("스케줄 내용"),
                                fieldWithPath("start").type(JsonFieldType.STRING).description("스케줄 시작 시간"),
                                fieldWithPath("end").type(JsonFieldType.STRING).description("스케줄 끝나는 시간"),
                                fieldWithPath("meetupId").type(JsonFieldType.NUMBER).description("밋업 아이디"),
                                fieldWithPath("managerId").type(JsonFieldType.STRING).description("스케줄 주인 아이디"),
                                fieldWithPath("managerName").type(JsonFieldType.STRING).description("스케줄 주인 이름"),
                                fieldWithPath("myWebex").type(JsonFieldType.STRING).description("스케줄 만든 사람 웹엑스"),
                                fieldWithPath("diffWebex").type(JsonFieldType.STRING).description("스케줄 받은 사람 웹엑스"),
                                fieldWithPath("type").type(JsonFieldType.STRING).description("스케줄 타입 (스케줄 or 미팅)"),
                                fieldWithPath("userId").type(JsonFieldType.STRING).description("스케줄(미팅) 신청한 사람 아이디"),
                                fieldWithPath("userName").type(JsonFieldType.STRING).description("스케줄(미팅) 신청한 사람 이름"),
                                fieldWithPath("delete").type(JsonFieldType.BOOLEAN).description("삭제 여부")
                        )
                ));
    }

    @Test
    public void createSchedule() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        ScheduleRequestDto scheduleRequestDto = ScheduleRequestDto.builder()
                .title("스케줄 제목")
                .content("점심 시간")
                .start("2022-11-18 12:00:00")
                .end("2022-11-18 13:00:00")
                .open(true)
                .build();

        Long scheduleId = 1L;
        given(scheduleService.createSchedule(anyString(), any(ScheduleRequestDto.class))).willReturn(scheduleId);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/schedule")
                .content(objectMapper.writeValueAsString(scheduleRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("schedule-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("스케줄 내용"),
                                fieldWithPath("start").type(JsonFieldType.STRING).description("스케줄 시작 시간"),
                                fieldWithPath("end").type(JsonFieldType.STRING).description("스케줄 끝나는 시간"),
                                fieldWithPath("open").type(JsonFieldType.BOOLEAN).description("스케줄 공개 여부")
                        ),
                        responseBody()
                ));
    }

    @Test
    public void updateSchedule() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        ScheduleUpdateRequestDto scheduleRequestDto = ScheduleUpdateRequestDto.builder()
                .id(1L)
                .title("스케줄 제목")
                .content("점심 시간")
                .start("2022-11-18 12:00:00")
                .end("2022-11-18 13:00:00")
                .open(true)
                .build();

        Long scheduleId = 1L;
        given(scheduleService.updateSchedule(anyString(), any(ScheduleUpdateRequestDto.class))).willReturn(scheduleId);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/schedule")
                .content(objectMapper.writeValueAsString(scheduleRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("schedule-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("스케줄 아이디"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("스케줄 내용"),
                                fieldWithPath("start").type(JsonFieldType.STRING).description("스케줄 시작 시간"),
                                fieldWithPath("end").type(JsonFieldType.STRING).description("스케줄 끝나는 시간"),
                                fieldWithPath("open").type(JsonFieldType.BOOLEAN).description("스케줄 공개 여부")
                        ),
                        responseBody()
                ));
    }

    @Test
    public void deleteSchedule() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        Long requestDto = 1L;
        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/schedule/{scheduleId}", requestDto))
                .andExpect(status().isOk())
                .andDo(document("schedule-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseBody()
                ));
    }

    @Test
    public void getScheduleByMeetupAndDate() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        List<Schedule> scheduleList = new ArrayList<>();
        List<Meeting> meetingList = new ArrayList<>();
        List<Meeting> partyMeetings = new ArrayList<>();

        scheduleList.add(new Schedule(
                LocalDateUtil.strToLDT("2022-11-15 13:00:00")
                , LocalDateUtil.strToLDT("2022-11-15 14:00:00")
                , "점심 약속"
                , "점심"
                , true
                , new User("userId", "password", "hong", "www.webex.com", ROLE_Coach, true)));

        scheduleList.add(new Schedule(
                LocalDateUtil.strToLDT("2022-11-16 15:00:00")
                , LocalDateUtil.strToLDT("2022-11-16 16:00:00")
                , "개인 스케줄"
                , "개인 스케줄입니다."
                , false
                , new User("userId", "password", "hong", "www.webex.com", ROLE_Coach, true)));

        scheduleList.add(Meeting.builder()
                .start(LocalDateUtil.strToLDT("2022-11-15 11:00:00"))
                .end(LocalDateUtil.strToLDT("2022-11-15 12:00:00"))
                .title("개인 미팅 신청")
                .content("개인 미팅 신청합니다.")
                .open(false)
                .user(new User("userId", "password", "hong", "www.webex2.com", ROLE_Coach, true))
                .meetup(new Meetup("전체", "#FFFFF", new User("pro", "password", "pro", "www.webex.com", ROLE_Coach, true), new Channel("channel1", "채널이름", "채널이름", ChannelType.Private, new Team("team1", "팀이름", "팀이름", TeamType.Invite))))
                .build());

        Meeting meeting1 = Meeting.builder()
                .start(LocalDateUtil.strToLDT("2022-11-16 15:00:00"))
                .end(LocalDateUtil.strToLDT("2022-11-16 16:00:00"))
                .title("개인 미팅 신청")
                .content("개인 미팅 신청합니다.")
                .open(false)
                .user(new User("user2", "password", "user2", "www.webex2.com", ROLE_Student, true))
                .meetup(new Meetup("2팀", "#FFFFF", new User("userId", "password", "hong", "www.webex.com", ROLE_Coach, true), new Channel("channel1", "채널이름", "채널이름", ChannelType.Private, new Team("team1", "팀이름", "팀이름", TeamType.Invite))))
                .build();
        Meeting meeting2 = Meeting.builder()
                .start(LocalDateUtil.strToLDT("2022-11-16 17:00:00"))
                .end(LocalDateUtil.strToLDT("2022-11-16 18:00:00"))
                .title("개인 미팅")
                .content("개인 미팅 신청합니다.")
                .open(false)
                .user(new User("user3", "password", "user3", "www.webex3.com", ROLE_Student, true))
                .meetup(new Meetup("3팀", "#FFFFF", new User("userId", "password", "hong", "www.webex.com", ROLE_Coach, true), new Channel("channel1", "채널이름", "채널이름", ChannelType.Private, new Team("team1", "팀이름", "팀이름", TeamType.Invite))))
                .build();
        meetingList.add(meeting1);
        meetingList.add(meeting2);

        partyMeetings.add(new Meeting(
                LocalDateUtil.strToLDT("2022-11-17 15:00:00")
                , LocalDateUtil.strToLDT("2022-11-17 16:00:00")
                , "2팀 미팅 신청"
                , "기획 피드백"
                , true
                , new User("user2", "password", "user2", "www.webex2.com", ROLE_Student, true)
                , new Meetup("2팀", "#FFFFF", new User("userId", "password", "hong", "www.webex.com", ROLE_Coach, true)
                , new Channel("channel1", "채널이름", "채널이름", ChannelType.Private, new Team("team1", "팀이름", "팀이름", TeamType.Invite)))
                , new Party("2팀")
        ));

        partyMeetings.add(new Meeting(
                LocalDateUtil.strToLDT("2022-11-18 15:00:00")
                , LocalDateUtil.strToLDT("2022-11-18 16:00:00")
                , "3팀 미팅 신청"
                , "주제 피드백"
                , true
                , new User("user7", "password", "user7", "www.webex7.com", ROLE_Student, true)
                , new Meetup("7팀", "#FFFFF", new User("userId", "password", "hong", "www.webex.com", ROLE_Coach, true)
                , new Channel("channel1", "채널이름", "채널이름", ChannelType.Private, new Team("team1", "팀이름", "팀이름", TeamType.Invite)))
                , new Party("7팀")
        ));


        AllScheduleResponseDto responseDto = AllScheduleResponseDto.of(scheduleList, meetingList, partyMeetings, userInfoDto.getId());
        String targetId = "coach1";
        String date = "2022-11-13 00:00:00";
        given(scheduleService.getScheduleByUserAndDate(userInfoDto.getId(), targetId, date))
                .willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/schedule")
                .param("targetId", targetId)
                .param("date", date))
                .andExpect(status().isOk())
                .andDo(document("target-user-schedule-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("meetingFromMe[]").type(JsonFieldType.ARRAY).description("신청한 미팅"),
                                fieldWithPath("meetingFromMe[].id").description("신청한 미팅 아이디"),
                                fieldWithPath("meetingFromMe[].open").type(JsonFieldType.BOOLEAN).description("신청한 미팅 공개여부"),
                                fieldWithPath("meetingFromMe[].start").type(JsonFieldType.STRING).description("신청한 미팅 시작 시간"),
                                fieldWithPath("meetingFromMe[].end").type(JsonFieldType.STRING).description("신청한 미팅 종료 시간"),
                                fieldWithPath("meetingFromMe[].title").type(JsonFieldType.STRING).description("신청한 미팅 제목"),
                                fieldWithPath("meetingFromMe[].content").type(JsonFieldType.STRING).description("신청한 미팅 내용"),
                                fieldWithPath("meetingFromMe[].userId").type(JsonFieldType.STRING).description("미팅을 신청한 사용자의 아이디(자기자신 or 그룹원)"),
                                fieldWithPath("meetingFromMe[].userName").type(JsonFieldType.STRING).description("미팅을 신청한 사용자의 이름(자기자신 or 그룹원)"),
                                fieldWithPath("meetingFromMe[].meetupName").type(JsonFieldType.STRING).description("신청한 미팅의 밋업 이름"),
                                fieldWithPath("meetingFromMe[].meetupColor").type(JsonFieldType.STRING).description("신청한 미팅의 밋업 색"),

                                fieldWithPath("meetingToMe[]").type(JsonFieldType.ARRAY).description("신청받은 미팅"),
                                fieldWithPath("meetingToMe[].id").description("신청받은 미팅 아이디"),
                                fieldWithPath("meetingToMe[].open").type(JsonFieldType.BOOLEAN).description("신청받은 미팅 공개여부"),
                                fieldWithPath("meetingToMe[].start").type(JsonFieldType.STRING).description("신청받은 미팅 시작 시간"),
                                fieldWithPath("meetingToMe[].end").type(JsonFieldType.STRING).description("신청받은 미팅 종료 시간"),
                                fieldWithPath("meetingToMe[].title").type(JsonFieldType.STRING).description("신청받은 미팅 제목"),
                                fieldWithPath("meetingToMe[].content").type(JsonFieldType.STRING).description("신청받은 미팅 내용"),
                                fieldWithPath("meetingToMe[].userId").type(JsonFieldType.STRING).description("미팅을 신청한 사용자의 아이디"),
                                fieldWithPath("meetingToMe[].userName").type(JsonFieldType.STRING).description("미팅을 신청한 사용자의 이름"),
                                fieldWithPath("meetingToMe[].meetupName").type(JsonFieldType.STRING).description("신청받은 미팅의 밋업 이름"),
                                fieldWithPath("meetingToMe[].meetupColor").type(JsonFieldType.STRING).description("신청받은 미팅의 밋업 색"),

                                fieldWithPath("scheduleResponseList[]").type(JsonFieldType.ARRAY).description("스케줄"),
                                fieldWithPath("scheduleResponseList[].id").description("스케줄 아이디"),
                                fieldWithPath("scheduleResponseList[].open").type(JsonFieldType.BOOLEAN).description("스케줄 공개여부"),
                                fieldWithPath("scheduleResponseList[].start").type(JsonFieldType.STRING).description("스케줄 시작시간"),
                                fieldWithPath("scheduleResponseList[].end").type(JsonFieldType.STRING).description("스케줄 종료시간"),
                                fieldWithPath("scheduleResponseList[].title").type(JsonFieldType.STRING).description("스케줄 제목"),
                                fieldWithPath("scheduleResponseList[].content").type(JsonFieldType.STRING).description("스케줄 내용"),
                                fieldWithPath("scheduleResponseList[].userId").type(JsonFieldType.STRING).description("스케줄 주인 아이디"),
                                fieldWithPath("scheduleResponseList[].userName").type(JsonFieldType.STRING).description("스케줄 주인 이름"),

                                fieldWithPath("partyMeetingResponseList[]").type(JsonFieldType.ARRAY).description("그룹 미팅"),
                                fieldWithPath("partyMeetingResponseList[].id").description("그룹 미팅 아이디"),
                                fieldWithPath("partyMeetingResponseList[].open").type(JsonFieldType.BOOLEAN).description("그룹 미팅 공개여부"),
                                fieldWithPath("partyMeetingResponseList[].start").type(JsonFieldType.STRING).description("그룹 미팅 시작시간"),
                                fieldWithPath("partyMeetingResponseList[].end").type(JsonFieldType.STRING).description("그룹 미팅 종료시간"),
                                fieldWithPath("partyMeetingResponseList[].title").type(JsonFieldType.STRING).description("그룹 미팅 제목"),
                                fieldWithPath("partyMeetingResponseList[].content").type(JsonFieldType.STRING).description("그룹 미팅 내용"),
                                fieldWithPath("partyMeetingResponseList[].userId").type(JsonFieldType.STRING).description("그룹 미팅 신청자 아이디"),
                                fieldWithPath("partyMeetingResponseList[].userName").type(JsonFieldType.STRING).description("그룹 미팅 신청자 이름"),
                                fieldWithPath("partyMeetingResponseList[].meetupName").type(JsonFieldType.STRING).description("그룹 미팅의 밋업 이름"),
                                fieldWithPath("partyMeetingResponseList[].meetupColor").type(JsonFieldType.STRING).description("그룹 미팅의 밋업 색"),
                                fieldWithPath("partyMeetingResponseList[].partyId").description("그룹 미팅의 그룹 아이디"),
                                fieldWithPath("partyMeetingResponseList[].partyName").type(JsonFieldType.STRING).description("그룹 미팅의 그룹 이름")
                        )
                ));
    }
}
