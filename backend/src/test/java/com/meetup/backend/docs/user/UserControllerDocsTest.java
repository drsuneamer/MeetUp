package com.meetup.backend.docs.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.backend.controller.UserController;
import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.dto.user.UserWebexInfoDto;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static com.meetup.backend.entity.user.RoleType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @Test
    public void login() throws Exception {
        //given
        LoginResponseDto responseDto = LoginResponseDto.of(
                User.builder()
                        .id("mmid")
                        .password("passwordpwd")
                        .nickname("hong")
                        .role(ROLE_Student)
                        .build()
                , new TokenDto("Bearer", "access-token", 1999999999L));


        LoginRequestDto loginRequestDto = new LoginRequestDto("mattermost@test.com", "mmpwd");

        given(userService.login(any(LoginRequestDto.class)))
                .willReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("user_login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("mattermost 계정 아이디"),
                                fieldWithPath("password").description("mattermost 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("mattermost 식별 아이디"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("roleType").type(JsonFieldType.STRING).description("권한"),
                                fieldWithPath("tokenDto.grantType").type(JsonFieldType.STRING).description("토큰 타입"),
                                fieldWithPath("tokenDto.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("tokenDto.tokenExpiresIn").type(JsonFieldType.NUMBER).description("토큰 만료 시간")

                        )
                ));
    }

    @Test
    public void logout() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("syngasdfe", "hong");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);


        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/user/logout"))
                .andExpect(status().isOk())
                .andDo(document("user-logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void setWebex() throws Exception {
        UserWebexInfoDto userWebexInfoDto = new UserWebexInfoDto("https://webexUrl");
        UserInfoDto userInfoDto = new UserInfoDto("userId", "userNickname");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/user/webex")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWebexInfoDto)))
                .andExpect(status().isCreated())
                .andDo(document("webex-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("webexUrl").description("적용할 webex URL")
                        )
                ));
    }

    @Test
    public void getMyWebex() throws Exception {
        UserInfoDto userInfoDto = new UserInfoDto("userId", "userNickname");
        UserWebexInfoDto userWebexInfoDto = new UserWebexInfoDto("webexUrl");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);
        given(userService.getWebexUrl("userId")).willReturn(userWebexInfoDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/user/webex"))
                .andExpect(status().isOk())
                .andDo(document("webex-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("webexUrl").description("현재 로그인중인 user의 webex URL")
                        )
                ));

    }

    @Test
    public void getWebex() throws Exception {
        UserWebexInfoDto userWebexInfoDto = new UserWebexInfoDto("webexUrl");
        given(userService.getWebexUrl("userId")).willReturn(userWebexInfoDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/user/webex/userId"))
                .andExpect(status().isOk())
                .andDo(document("webex-read-by-userId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseBody()
                ));

    }

}
