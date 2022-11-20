package com.meetup.backend.docs.meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.backend.controller.MeetingController;
import com.meetup.backend.dto.schedule.meeting.MeetingChannelDto;
import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.meeting.MeetingService;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import springfox.documentation.spring.web.json.Json;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
class MeetingControllerDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private ChannelUserService channelUserService;

    @MockBean
    private AuthService authService;

    @Test
    public void createMeeting() throws Exception {
        // given
        MeetingRequestDto meetingRequestDto = MeetingRequestDto.builder()
                .title("미팅 제목")
                .content("미팅 내용")
                .start("2022-11-15T16:00:00")
                .end("2022-11-15T18:00:00")
                .meetupId(1L)
                .open(true)
                .partyId(null)
                .build();
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        given(meetingService.createMeeting(anyString(), any(MeetingRequestDto.class)))
                .willReturn(1L);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/meeting")
                .content(objectMapper.writeValueAsString(meetingRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("meeting-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("미팅 제목"),
                                fieldWithPath("content").description("미팅 내용"),
                                fieldWithPath("start").description("미팅의 시작 시간"),
                                fieldWithPath("end").description("미팅의 종료 시간"),
                                fieldWithPath("meetupId").description("해당 미팅의 밋업 ID"),
                                fieldWithPath("open").description("미팅의 공개 여부"),
                                fieldWithPath("partyId").description("미팅의 그룹 소속 ID")
                        ),
                        responseBody()
                        )
                );
    }

    @Test
    public void updateMeeting() throws Exception {
        // given
        MeetingUpdateRequestDto meetingUpdateRequestDto = MeetingUpdateRequestDto.builder()
                .id(1L)
                .title("미팅 제목")
                .content("미팅 내용")
                .start("2022-11-15T16:00:00")
                .end("2022-11-15T18:00:00")
                .meetupId(1L)
                .open(true)
                .build();
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        given(meetingService.updateMeeting(anyString(), any(MeetingUpdateRequestDto.class)))
                .willReturn(1L);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/meeting")
                .content(objectMapper.writeValueAsString(meetingUpdateRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isCreated()))
                .andDo(document("meeting-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("미팅 아이디"),
                                fieldWithPath("title").description("미팅 제목"),
                                fieldWithPath("content").description("미팅 내용"),
                                fieldWithPath("start").description("미팅의 시작 시간"),
                                fieldWithPath("end").description("미팅의 종료 시간"),
                                fieldWithPath("meetupId").description("해당 미팅의 밋업 ID"),
                                fieldWithPath("open").description("미팅의 공개 여부")
                        ),
                        responseBody()
                        )
                );
    }

    @Test
    public void deleteMeeting() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        Long requestDto = 1L;
        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/meeting/{meetingId}", requestDto))
                .andExpect((status().isOk()))
                .andDo(document("meeting-delete",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseBody()
                        )
                );
    }

    @Test
    public void getAlertChannelList() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        List<MeetingChannelDto> meetingChannelDtoList = new ArrayList<>();
        meetingChannelDtoList.add(MeetingChannelDto.builder().meetupId(1L).displayName("1팀").build());
        meetingChannelDtoList.add(MeetingChannelDto.builder().meetupId(2L).displayName("2팀").build());
        meetingChannelDtoList.add(MeetingChannelDto.builder().meetupId(3L).displayName("3팀").build());
        given(channelUserService.getMeetingChannelByUsers(anyString(), anyString()))
                .willReturn(meetingChannelDtoList);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/meeting/channel/{managerId}", "managerId"))
                .andExpect(status().isOk())
                .andDo(document("channel-list-find-by-managerId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("managerId").description("조회할 managerId")
                        ),

                        responseFields(
                                fieldWithPath("[].meetupId").type(JsonFieldType.NUMBER).description("meetup 아이디"),
                                fieldWithPath("[].displayName").type(JsonFieldType.STRING).description("채널 이름")
                        )
                ));
    }
}
