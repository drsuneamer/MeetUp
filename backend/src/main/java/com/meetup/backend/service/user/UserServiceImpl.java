package com.meetup.backend.service.user;

import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.jwt.JwtTokenProvider;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.redis.RedisUtil;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by seongmin on 2022/10/23
 * updated by seongmin on 2022/10/25
 * updated by seungyong on 2022/10/27
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtil redisUtil;

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        MattermostClient client = Client.getClient();
        Response mmLoginResponse = client.login(requestDto.getId(), requestDto.getPassword()).getRawResponse();
        switch (mmLoginResponse.getStatus()) {
            case 200:
                JSONObject jsonRes = JsonConverter.toJson((BufferedInputStream) mmLoginResponse.getEntity());
                String id = (String) jsonRes.get("id");
                String mmToken = mmLoginResponse.getHeaderString("Token");

                String nickname = (String) jsonRes.get("nickname");
                User user;
                if (userRepository.findById(id).isEmpty()) {
                    user = userRepository.save(
                            User.builder()
                                    .id(id)
                                    .role(RoleType.Student)
                                    .nickname(nickname)
                                    .password(passwordEncoder.encode(requestDto.getPassword()))
                                    .build());
                } else {
                    user = userRepository.findById(id).get();
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, requestDto.getPassword());

                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                TokenDto tokenDto = jwtTokenProvider.generateJwtToken(authentication);

                redisUtil.setData(authentication.getName(), mmToken, tokenDto.getTokenExpiresIn(), TimeUnit.MILLISECONDS);
                return LoginResponseDto.of(user, tokenDto);
            case 401:
                throw new ApiException(EMPTY_CREDENTIAL);
            default:
                throw new ApiException(MATTERMOST_EXCEPTION);
        }
    }

    @Override
    public void logout(String mmSessionToken) {
        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);
        int status = client.logout().getRawResponse().getStatus();
        if (status == 400) {
            throw new ApiException(BAD_REQUEST_LOGOUT);
        } else if (status == 403) {
            throw new ApiException(ACCESS_DENIED);
        }
    }

    @Override
    public void registerUserFromList(List<User> userList) {

        for (User user : userList) {
            if (userRepository.findById(user.getId()).isEmpty()) {
                userRepository.save(user);
            }
        }

    }
}
