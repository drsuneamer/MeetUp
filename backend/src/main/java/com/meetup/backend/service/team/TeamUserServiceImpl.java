package com.meetup.backend.service.team;

import com.meetup.backend.dto.team.TeamActivateRequestDto;
import com.meetup.backend.dto.team.TeamActivateResponseDto;
import com.meetup.backend.dto.team.TeamResponseDto;
import com.meetup.backend.dto.user.UserListInTeamResponseDto;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.util.converter.JsonConverter;
import com.meetup.backend.util.exception.MattermostEx;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.client4.Pager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by myeongseok on 2022/10/21
 * updated by seongmin on 2022/11/04
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TeamUserServiceImpl implements TeamUserService {

    @Autowired
    private final TeamUserRepository teamUserRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TeamRepository teamRepository;

    @Override
    public List<TeamResponseDto> getTeamByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();

        for (TeamUser teamUser : teamUserRepository.findByUser(user)) {
            if (!teamUser.isActivate())
                continue;
            teamResponseDtoList.add(TeamResponseDto.of(teamUser.getTeam()));
        }

        return teamResponseDtoList;
    }

    // db에 저장되어 있지 않은 팀만 TeamUser db 저장
    // Teamuser에 이미 저장되어 있는지 확인 할 필요 없음
    @Override
    public void registerTeamUserFromMattermost(String mmSessionToken, List<Team> teamList) {

        MattermostClient client = Client.getClient();
        client.setAccessToken(mmSessionToken);

        for (Team team : teamList) {
            List<User> userList = new ArrayList<>();
            for (int k = 0; ; k++) {

                Response mmTeamUserResponse = client.getTeamMembers(team.getId(), Pager.of(k, 200)).getRawResponse();

                MattermostEx.apiException(mmTeamUserResponse.getStatus());

                JSONArray userArray = new JSONArray();

                try {
                    userArray = JsonConverter.toJsonArray((BufferedInputStream) mmTeamUserResponse.getEntity());
                } catch (ClassCastException e) {
                    log.error(e.getMessage());
                    log.info("mmChannelUserResponse.getEntity() = {}", mmTeamUserResponse.getEntity());
                    e.printStackTrace();
                }
                if (userArray.isEmpty()) break;

                for (int l = 0; l < userArray.length(); l++) {

                    String userId = userArray.getJSONObject(l).getString("user_id");

                    // @id 값을 알고있기 때문에 persist()가 아닌 merge()를 함.
                    // insert 쿼리 전 select 실행?
                    userList.add(userRepository.findById(userId).orElse(User.builder()
                            .id(userId)
                            .firstLogin(false)
                            .role(RoleType.ROLE_Student)
                            .build()));

//                    User user = userRepository.findById(userId).orElseGet(
//                            () -> userRepository.save(
//                                    User.builder()
//                                            .id(userId)
//                                            .firstLogin(false)
//                                            .role(RoleType.Student)
//                                            .build()
//                            )
//                    );
//                    if (teamUserRepository.findByTeamAndUser(team, user).isEmpty()) {
//                        TeamUser teamUser = TeamUser.builder().team(team).user(user).build();
//                        teamUserRepository.save(teamUser);
//                    }
                }
            }

            Set<User> userSet = new HashSet<>(userRepository.findAll());
            userRepository.saveAll(userList.stream().filter(user -> !userSet.contains(user)).collect(Collectors.toList()));
//            List<TeamUser> teamUserList = userList.stream().map(user -> TeamUser.builder().team(team).user(user).build())
//                    .filter(teamUser -> teamUserRepository.existsByUserAndTeam(teamUser.getUser(), teamUser.getTeam()))
//                    .collect(Collectors.toList());

//            teamUserRepository.saveAll(userList.stream().filter(user -> !teamUserRepository.existsByUserAndTeam(user, team))
//                    .map(user -> TeamUser.builder().team(team).user(user).build())
//                    .collect(Collectors.toList()));
            Set<TeamUser> teamUserSet = new HashSet<>(teamUserRepository.findByTeam(team));
            teamUserRepository.saveAll(userList.stream()
                    .map(user -> TeamUser.builder().team(team).user(user).build())
                    .filter(teamUser -> !teamUserSet.contains(teamUser))
                    .collect(Collectors.toList()));

        }
    }

    @Override
    public List<TeamActivateResponseDto> getActivateTeamByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));

        List<TeamActivateResponseDto> teamActivateResponseDtoList = new ArrayList<>();

        for (TeamUser teamUser : teamUserRepository.findByUser(user)) {
            teamActivateResponseDtoList.add(TeamActivateResponseDto.of(teamUser));
        }

        return teamActivateResponseDtoList;
    }

    @Override
    public void activateTeamUser(String userId, List<TeamActivateRequestDto> teamActivateRequestDtoList) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));

        for (TeamActivateRequestDto teamActivateRequestDto : teamActivateRequestDtoList) {
            Team team = teamRepository.findById(teamActivateRequestDto.getTeamId()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));

            TeamUser teamUser = teamUserRepository.findByTeamAndUser(team, user).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_USER_NOT_FOUND));
            teamUser.changeActivate();
        }


    }

    @Override
    public List<UserListInTeamResponseDto> getUserByTeam(String mmSessionToken, String teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamUser> teamUserList = teamUserRepository.findByTeam(team);

        List<UserListInTeamResponseDto> userListInTeamResponseDtoList = new ArrayList<>();
        for (TeamUser teamUser : teamUserList) {
            User user = teamUser.getUser();
            if (user.getNickname() == null) {
                MattermostClient client = Client.getClient();
                client.setAccessToken(mmSessionToken);
                Response response = client.getUser(user.getId()).getRawResponse();
                JSONObject userObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());
                user.setNickname(userObj.getString("nickname"));
            }
            userListInTeamResponseDtoList.add(UserListInTeamResponseDto.of(user));
        }
        return userListInTeamResponseDtoList;
    }
}
