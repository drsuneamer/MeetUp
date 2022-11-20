package com.meetup.backend.dto.admin;

import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * created by seungyong on 2022/10/31
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponseDto {

    private String id;
    private String nickname;
    private RoleType role;

    public static List<UserResponseDto> of(List<User> users) {
        List<UserResponseDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserResponseDto(user.getId(), user.getNickname(), user.getRole()));
        }
        return result;
    }
}
