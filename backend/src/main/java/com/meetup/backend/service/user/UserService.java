package com.meetup.backend.service.user;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.entity.user.User;

/**
 * created by seongmin on 2022/10/23
 * updated by seongmin on 2022/10/30
 */
public interface UserService {
    LoginResponseDto login(LoginRequestDto requestDto);

    void logout(String id);

//    User registerUser(String userId);

    void registerTeamAndChannel(String mmToken, User user);
}
