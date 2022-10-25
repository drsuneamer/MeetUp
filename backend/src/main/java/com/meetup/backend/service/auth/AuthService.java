package com.meetup.backend.service.auth;

import com.meetup.backend.dto.user.UserInfoDto;

/**
 * created by seongmin on 2022/10/25
 */
public interface AuthService {

    UserInfoDto getMyInfoSecret();
    String getMMSessionToken(String accessToken);
}
