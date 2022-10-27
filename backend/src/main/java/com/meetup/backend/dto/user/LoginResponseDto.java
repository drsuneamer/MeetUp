package com.meetup.backend.dto.user;

import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by seongmin on 2022/10/23
 */
@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private String id;
    private String nickname;
    private TokenDto tokenDto;

    public static LoginResponseDto of(User user, TokenDto tokenDto) {
        return new LoginResponseDto(user.getId(), user.getNickname(), tokenDto);
    }
}
