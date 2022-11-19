package com.meetup.backend.docs.party;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.backend.controller.PartyController;
import com.meetup.backend.dto.party.PartyRequestDto;
import com.meetup.backend.dto.party.PartyResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.party.PartyService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartyController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class PartyControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private PartyService partyService;

    @Test
    public void createParty() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        List<String> members = new ArrayList<>();
        members.add("user1");
        members.add("user2");
        members.add("user3");
        PartyRequestDto requestDto = new PartyRequestDto("2팀", members);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/group")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("group-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("그룹 이름"),
                                fieldWithPath("members[]").description("그룹에 초대할 사용자들")
                        ),
                        responseBody()
                        )
                );
    }

    @Test
    public void getMyParty() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        List<PartyResponseDto> responseDto = new ArrayList<>();
        responseDto.add(new PartyResponseDto(1L, "1팀", true));
        responseDto.add(new PartyResponseDto(2L, "2팀", false));
        responseDto.add(new PartyResponseDto(2L, "3팀", false));

        given(partyService.getMyParty(anyString()))
                .willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/group"))
                .andExpect(status().isOk())
                .andDo(document("my-group-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("그룹 아이디"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("그룹 이름"),
                                fieldWithPath("[].leader").type(JsonFieldType.BOOLEAN).description("내가 이 그룹의 리더인지 확인")
                        ))
                );
    }

    @Test
    public void getPartyMembers() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        List<UserInfoDto> responseDto = new ArrayList<>();
        responseDto.add(new UserInfoDto("user1", "kim"));
        responseDto.add(new UserInfoDto("user2", "lee"));
        responseDto.add(new UserInfoDto("user3", "park"));

        given(partyService.getPartyMembers(anyString(), anyLong()))
                .willReturn(responseDto);

        Long requestDto = 1L;

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/group/{id}", requestDto))
                .andExpect(status().isOk())
                .andDo(document("group-member-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.STRING).description("멤버 아이디"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("멤버 이름")
                        ))
                );
    }

    @Test
    public void deleteParty() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("userId", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        Long requestDto = 1L;

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/group/{id}", requestDto))
                .andExpect(status().isOk())
                .andDo(document("group-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseBody()
                ));
    }
}
