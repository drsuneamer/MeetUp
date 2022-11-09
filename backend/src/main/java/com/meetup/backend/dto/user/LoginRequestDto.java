package com.meetup.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * created by seongmin on 2022/10/23
 * updated by seongmin on 2022/11/09
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LoginRequestDto {
    @Email
    @NotBlank
    private String id;
    @NotBlank
    private String password;
}
