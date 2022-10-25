package com.meetup.backend.dto.user;

import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by seongmin on 2022/10/25
 */
@AllArgsConstructor
@Getter
public class UserInfoDto {
    private String id;
    private String nickname;

    public static UserInfoDto of(User user) {
        return new UserInfoDto(user.getId(), user.getNickname());
    }
}
