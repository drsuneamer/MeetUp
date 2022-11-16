package com.meetup.backend.docs.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.backend.controller.AdminController;
import com.meetup.backend.dto.admin.AdminLoginDto;
import com.meetup.backend.dto.admin.SignUpDto;
import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.service.admin.AdminService;
import com.meetup.backend.service.auth.AuthService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @MockBean
    private AuthService authService;

    @Test
    public void signup() throws Exception {
        // given
        SignUpDto signUpDto = SignUpDto.builder()
                .id("admin")
                .password("admin")
                .key("asklfaejfkasldf")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/signup")
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("admin-signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("admin 계정 아이디"),
                                fieldWithPath("password").description("admin 비밀번호"),
                                fieldWithPath("key").description("admin key")
                        )
                ));
    }

    @Test
    public void login() throws Exception {
        // given
        AdminLoginDto adminLoginDto = AdminLoginDto.builder()
                .id("admin")
                .password("admin")
                .build();

        TokenDto responseDto = TokenDto.builder()
                .grantType("Bearer")
                .accessToken("access-token")
                .tokenExpiresIn(1999999999L)
                .build();

        given(adminService.login(anyString(), anyString())).willReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/login")
                        .content(objectMapper.writeValueAsString(adminLoginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("admin 계정 아이디"),
                                fieldWithPath("password").description("admin 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(JsonFieldType.STRING).description("토큰 타입"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("tokenExpiresIn").type(JsonFieldType.NUMBER).description("토큰 만료 시간")
                        )
                ));
    }

    @Test
    public void changeRole() throws Exception {

    }

    @Test
    public void getUsers() throws Exception {

    }
}
