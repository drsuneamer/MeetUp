package com.meetup.backend.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by seongmin on 2022/10/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;
}
