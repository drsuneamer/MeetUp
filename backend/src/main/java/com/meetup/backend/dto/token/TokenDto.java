package com.meetup.backend.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by seongmin on 2022/10/20
 * updated by seongmin on 2022/10/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;

    // mm token 유효시간이 12시간, refreshToken 사용 안함
//    private String refreshToken;
//    private Long refreshTokenExpiresIn;
}
