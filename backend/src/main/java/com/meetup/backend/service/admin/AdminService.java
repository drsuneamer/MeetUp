package com.meetup.backend.service.admin;

import com.meetup.backend.dto.admin.ChangeRoleDto;
import com.meetup.backend.dto.admin.UserResponseDto;
import com.meetup.backend.dto.token.TokenDto;

import java.util.List;

/**
 * created by seongmin on 2022/10/31
 */
public interface AdminService {
    void signUp(String id, String password);
    TokenDto login(String id, String password);
    void changeRole(String userId, List<ChangeRoleDto> changeRoleDtoList);

    List<UserResponseDto> getUsers(String id);
}
