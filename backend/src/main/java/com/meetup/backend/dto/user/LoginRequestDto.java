package com.meetup.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * created by seongmin on 2022/10/23
 * updated by seongmin on 2022/11/14
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LoginRequestDto {
    @Email(message = "id는 이메일 형식이어야 합니다.")
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private String id;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
