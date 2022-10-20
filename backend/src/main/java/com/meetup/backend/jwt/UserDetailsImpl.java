package com.meetup.backend.jwt;

import com.meetup.backend.entity.user.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * created by seongmin on 2022/10/20
 */
@Getter
@Slf4j
public class UserDetailsImpl implements UserDetails {
    private String id;
    private String email;
    private String nickname;

    public UserDetailsImpl(String id, String email,String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() { // pwd 없음
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
