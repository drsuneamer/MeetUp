package com.meetup.backend.dto.user;

import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * created by seongmin on 2022/10/25
 * updated by seongmin on 2022/11/11
 */
@AllArgsConstructor
@Getter
@Setter
public class UserInfoDto {
    private String id;
    private String nickname;

    public static UserInfoDto of(User user) {
        return new UserInfoDto(user.getId(), user.getNickname());
    }

    public static UserInfoDto of(String id, String nickname) {
        return new UserInfoDto(id, nickname);
    }
}
