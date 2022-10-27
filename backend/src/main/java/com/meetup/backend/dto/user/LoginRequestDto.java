package com.meetup.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * created by seongmin on 2022/10/23
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @Email
    @NotBlank
    private String id;
    @NotBlank
    private String password;
}
