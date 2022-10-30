package com.meetup.backend.service.admin;

import com.meetup.backend.dto.token.TokenDto;

/**
 * created by seongmin on 2022/10/31
 */
public interface AdminService {
    void signUp(String id, String password);
    TokenDto login(String id, String password);
}
