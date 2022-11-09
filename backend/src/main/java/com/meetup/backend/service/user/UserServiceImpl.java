package com.meetup.backend.service.user;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.meetup.backend.dto.channel.ChannelCreateRequestDto;
import com.meetup.backend.dto.token.TokenDto;
import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.dto.user.UserWebexInfoDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.jwt.JwtTokenProvider;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.channel.ChannelService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.notice.WebhookNoticeService;
import com.meetup.backend.service.team.TeamService;
import com.meetup.backend.service.team.TeamUserService;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.redis.RedisUtil;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.meetup.backend.entity.user.RoleType.*;
import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by seongmin on 2022/10/23
 * updated by seongmin on 2022/11/09
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ChannelUserRepository channelUserRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    //    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final TeamService teamService;
    private final ChannelService channelService;
    private final TeamUserService teamUserService;
    private final ChannelUserService channelUserService;
    private final WebhookNoticeService webhookNoticeService;

    @Value("${crypto.secret}")
    private final String secretKey;

    @Value("${crypto.iv}")
    private final String iv;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(secretKey.getBytes(), "AES"),
                    new IvParameterSpec(iv.getBytes()));

            String originPwd = new String(cipher.doFinal(Base64.getDecoder().decode(requestDto.getPassword().getBytes("UTF-8"))));
            requestDto.setPassword(originPwd);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ApiException(PASSWORD_DECRYPTION_ERROR);
        }

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
                                    .role(ROLE_Student)
                                    .nickname(nickname)
                                    .password(passwordEncoder.encode(requestDto.getPassword()))
                                    .firstLogin(false)
                                    .build());
                } else {
                    user = userRepository.findById(id).get();
                    if (!nickname.equals(user.getNickname())) {
                        user.setNickname(nickname);
                    }
                    String encodedPwd = passwordEncoder.encode(requestDto.getPassword());
                    if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                        log.info("패스워드 변경");
                        user.changePwd(encodedPwd);
                    }
                }

                if (!user.isFirstLogin()) {
                    if (nickname.contains("컨설턴트") || nickname.contains("consultant")) {
                        user.changeRole(ROLE_Consultant);
                    } else if (nickname.contains("교수")) {
                        user.changeRole(ROLE_Professor);
                    } else if (nickname.contains("코치")) {
                        user.changeRole(ROLE_Coach);
                    } else if (nickname.contains("프로")) {
                        user.changeRole(ROLE_Pro);
                    }
                    webhookNoticeService.firstLoginNotice(nickname);
                    user.setFirstLogin();
                    registerTeamAndChannel(mmToken, user);
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, requestDto.getPassword());

                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                TokenDto tokenDto = jwtTokenProvider.generateJwtToken(authentication, mmToken);

//                redisUtil.setData(authentication.getName(), mmToken, TimeUnit.MILLISECONDS.toSeconds(tokenDto.getTokenExpiresIn()), TimeUnit.SECONDS);
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
//        redisUtil.deleteData(id);
    }

    @Override
    public String getNickname(String userId, String mmSessionToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        if (user.getNickname() != null) {
            return user.getNickname();
        } else {
            MattermostClient client = Client.getClient();
            client.setAccessToken(mmSessionToken);
            Response response = client.getUser(userId).getRawResponse();
            JSONObject userObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());
            return userObj.getString("nickname");
        }
    }
//
//    @Override
//    public User registerUser(String userId) {
//
//        if (userRepository.findById(userId).isEmpty()) {
//            User user = User.builder().id(userId).build();
//            userRepository.save(user);
//            return user;
//        }
//
//        return userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
//
//    }

    @Override
    public void registerTeamAndChannel(String mmToken, User user) {
        List<Team> teams = teamService.registerTeamFromMattermost(user.getId(), mmToken); // 팀 등록
        teamUserService.registerTeamUserFromMattermost(mmToken, teams);
        List<Channel> channels = channelService.registerChannelFromMattermost(user.getId(), mmToken, teams);
        channelUserService.registerChannelUserFromMattermost(mmToken, channels);

        if (user.getRole().equals(ROLE_Consultant)) {
            for (Team team : teams) {
                if (!team.getDisplayName().contains("자율"))
                    continue;
                String teamId = team.getId();
                for (Channel channel : channelRepository.findByTeam(Team.builder().id(teamId).build())) {
                    if (!channel.getName().equals("town-square"))
                        continue;
                    String channelId = channel.getId();
                    List<ChannelUser> channelUserList = channelUserRepository.findByChannel(Channel.builder().id(channelId).build());
                    List<String> inviteList = new ArrayList<>();
                    for (ChannelUser channelUser : channelUserList) {
                        inviteList.add(channelUser.getUser().getId());
                    }
                    channelService.createNewChannel(user.getId(), mmToken, ChannelCreateRequestDto.builder()
                            .teamId(teamId)
                            .displayName("MeetUp! 테스트용 채널")
                            .type(ChannelType.Open)
                            .name("meetup__test_channel")
                            .inviteList(inviteList)
                            .build());
                }
            }
        }

    }

    @Override
    @Transactional
    public void changeWebexUrl(String userId, String webexUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        user.setWebex(webexUrl);
    }

    @Override
    public UserWebexInfoDto getWebexUrl(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        return UserWebexInfoDto.of(userRepository.findById(user.getId()).get().getWebex());
    }

}
