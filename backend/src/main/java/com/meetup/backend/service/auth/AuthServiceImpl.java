package com.meetup.backend.service.auth;

import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.util.redis.RedisUtil;
import com.meetup.backend.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * created by seongmin on 2022/10/25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    @Override
    public UserInfoDto getMyInfoSecret() {
        User user = userRepository.findById(SecurityUtil.getCurrentId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        return UserInfoDto.of(user);
    }

    @Override
    public String getMMSessionToken(String id) {
        return redisUtil.getData(id);
    }
}
