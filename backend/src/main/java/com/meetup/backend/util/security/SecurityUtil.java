package com.meetup.backend.util.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * created by seongmin on 2022/10/20
 */
public class SecurityUtil {
    private SecurityUtil() { }

    public static String getCurrentId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return authentication.getName();
    }
}
