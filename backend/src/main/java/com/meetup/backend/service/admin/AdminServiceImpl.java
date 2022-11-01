package com.meetup.backend.service.admin;

import com.meetup.backend.dto.admin.ChangeRoleDto;
import com.meetup.backend.dto.admin.UserResponseDto;
import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.jwt.JwtTokenProvider;
import com.meetup.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by seongmin on 2022/10/31
 * updated by seongmin on 2022/11/01
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    @Transactional
    public void signUp(String id, String password) {
        User user = User.builder().id(id).password(passwordEncoder.encode(password)).role(RoleType.Admin).build();
        if (userRepository.findById(id).isPresent()) {
            throw new ApiException(DUPLICATE_ID);
        }
        userRepository.save(user);
    }

    @Override
    public TokenDto login(String id, String password) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
            if (!userRepository.findById(id).orElseThrow(() -> new ApiException(USER_NOT_FOUND)).getRole().equals(RoleType.Admin)) {
                throw new ApiException(ADMIN_ACCESS_DENIED);
            }
            return jwtTokenProvider.generateJwtToken(authentication);
        } catch (BadCredentialsException e) {
            throw new ApiException(ID_PWD_NOT_MATCHING);
        }
    }

    @Override
    @Transactional
    public void changeRole(String userId, List<ChangeRoleDto> changeRoleDtoList) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        if (!user.getRole().equals(RoleType.Admin)) {
            throw new ApiException(ADMIN_ACCESS_DENIED);
        }
        for (ChangeRoleDto changeRoleDto : changeRoleDtoList) {
            User u = userRepository.findById(changeRoleDto.getId()).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
            u.changeRole(changeRoleDto.getRoleType());
        }
    }

    @Override
    public List<UserResponseDto> getUsers(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        if (!user.getRole().equals(RoleType.Admin)) {
            throw new ApiException(ADMIN_ACCESS_DENIED);
        }
        List<User> users = userRepository.findByFirstLogin(true);
        return UserResponseDto.of(users);
    }
}
