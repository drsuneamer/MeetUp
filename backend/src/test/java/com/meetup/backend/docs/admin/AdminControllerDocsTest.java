package com.meetup.backend.docs.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.backend.controller.AdminController;
import com.meetup.backend.dto.admin.AdminLoginDto;
import com.meetup.backend.dto.admin.ChangeRoleDto;
import com.meetup.backend.dto.admin.SignUpDto;
import com.meetup.backend.dto.admin.UserResponseDto;
import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.service.admin.AdminService;
import com.meetup.backend.service.auth.AuthService;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.key}")
    private String adminKey;

    @Test
    public void signup() throws Exception {
        // given
        SignUpDto signUpDto = SignUpDto.builder()
                .id("admin")
                .password("admin")
                .key(adminKey)
                .build();

        // when, then
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

        // when, then
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
        // given
        UserInfoDto userInfoDto = new UserInfoDto("admin", "admin");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        List<ChangeRoleDto> changeRoleDtoList = new ArrayList<>();
        changeRoleDtoList.add(new ChangeRoleDto("coach", ROLE_Coach));
        changeRoleDtoList.add(new ChangeRoleDto("consultant", ROLE_Consultant));
        changeRoleDtoList.add(new ChangeRoleDto("pro", ROLE_Pro));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/role")
                .content(objectMapper.writeValueAsString(changeRoleDtoList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("admin-change-role",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("[].id").description("권한을 변경할 사용자 아이디"),
                                fieldWithPath("[].roleType").description("변경할 권한")
                        ),
                        responseBody()
                ));
    }

    @Test
    public void getUsers() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto("admin", "admin");
        given(authService.getMyInfoSecret()).willReturn(userInfoDto);

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        userResponseDtoList.add(new UserResponseDto("coach", "코치", ROLE_Coach));
        userResponseDtoList.add(new UserResponseDto("pro", "프로", ROLE_Pro));
        userResponseDtoList.add(new UserResponseDto("consultant", "컨설턴트", ROLE_Consultant));
        userResponseDtoList.add(new UserResponseDto("student1", "학생1", ROLE_Student));
        userResponseDtoList.add(new UserResponseDto("student2", "학생2", ROLE_Student));
        given(adminService.getUsers(anyString())).willReturn(userResponseDtoList);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin"))
                .andExpect(status().isOk())
                .andDo(document("get-user-role-list-by-admin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.STRING).description("사용자 아이디"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
                                fieldWithPath("[].role").type(JsonFieldType.STRING).description("사용자 권한")
                        )
                ));
    }
}
