package com.meetup.backend.service.user;

import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.UnAuthorizedException;
import com.meetup.backend.jwt.JwtTokenProvider;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.redis.RedisUtil;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.util.concurrent.TimeUnit;

/**
 * created by seongmin on 2022/10/23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtil redisUtil;

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        MattermostClient client = Client.getClient();
        Response mmLoginResponse = client.login(requestDto.getId(), requestDto.getPassword()).getRawResponse();
        switch (mmLoginResponse.getStatus()) {
            case 200:
                JSONObject jsonRes = JsonConverter.toJson((BufferedInputStream) mmLoginResponse.getEntity());
                String id = (String) jsonRes.get("id");
                String mmToken = mmLoginResponse.getHeaderString("Token");

                // 닉네임 저장은 나중에..
                String nickname = (String) jsonRes.get("nickname");
                User user;
                if (userRepository.findById(id).isEmpty()) {
                    user = userRepository.save(User.builder().id(id).role(RoleType.Student).build());
                } else {
                    user = userRepository.findById(id).get();
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getId(), requestDto.getPassword());

                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                TokenDto tokenDto = jwtTokenProvider.generateJwtToken(authentication);

                redisUtil.setData(authentication.getName(), mmToken, tokenDto.getTokenExpiresIn(), TimeUnit.MILLISECONDS);

                return LoginResponseDto.of(user, tokenDto);

            case 401:
                throw new UnAuthorizedException("전자우편 주소 또는 사용자명이나 비밀번호를 잘못 입력하셨습니다.");
            default:
                throw new IllegalArgumentException("잠시 후 다시 시도해주세요.");
        }
    }
}
