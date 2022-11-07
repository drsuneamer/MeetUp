package com.meetup.backend.dto.user;

import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by seungyong on 2022/11/07
 */
@Getter
@AllArgsConstructor
public class UserListInTeamResponseDto {

    private String id;
    private String nickname;

    public static UserListInTeamResponseDto of(User user) {
        return new UserListInTeamResponseDto(user.getId(), user.getNickname());
    }

}
