package com.meetup.backend.service.user;

import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;

/**
 * created by seongmin on 2022/10/23
 */
public interface UserService {
    LoginResponseDto login(LoginRequestDto requestDto);
}
