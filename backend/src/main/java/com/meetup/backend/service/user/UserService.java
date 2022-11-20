package com.meetup.backend.service.user;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.dto.user.UserWebexInfoDto;
import com.meetup.backend.entity.user.User;

/**
 * created by seongmin on 2022/10/23
 * updated by seongmin on 2022/11/15
 */
public interface UserService {
    LoginResponseDto login(LoginRequestDto requestDto);

    void logout(String mmSessionToken);

    String getNickname(String userId, String mmSessionToken);

    void registerTeamAndChannel(String mmToken, String userId);

    void changeWebexUrl(String userId, String webexUrl);

    UserWebexInfoDto getWebexUrl(String userId);
}
