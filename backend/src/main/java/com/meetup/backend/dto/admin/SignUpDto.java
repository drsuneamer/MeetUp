package com.meetup.backend.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * created by seongmin on 2022/10/31
 */
@Data
public class SignUpDto {
    @NotEmpty(message = "id는 필수 입력 값입니다.")
    private String id;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotEmpty(message = "key는 필수 입력 값입니다.")
    private String key;
}
