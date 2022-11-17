package com.meetup.backend.docs.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.backend.controller.ScheduleController;
import com.meetup.backend.dto.schedule.ScheduleResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.schedule.ScheduleType;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.meeting.ScheduleService;
import com.meetup.backend.util.converter.LocalDateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
        UserInfoDto userInfoDto = new UserInfoDto("11", "qwer");
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
                                fieldWithPath("userName").type(JsonFieldType.STRING).description("스케줄(미팅) 신청한 사람 이름")
                        )
                ));
    }
}
