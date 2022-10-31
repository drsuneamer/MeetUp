package com.meetup.backend.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * created by seongmin on 2022/10/31
 */
@Data
public class AdminLoginDto {
    @NotEmpty(message = "아이디는 비어있을 수 없습니다.")
    private String id;
    @NotEmpty(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;
}
