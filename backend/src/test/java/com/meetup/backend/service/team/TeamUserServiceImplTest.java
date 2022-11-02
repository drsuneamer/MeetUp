package com.meetup.backend.service.team;

import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.repository.team.TeamUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
class TeamUserServiceImplTest {

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Test
    @DisplayName("사용자가 포함된 팀 목록을 반환")
    void getTeamByUser() {

        String userId="pfnfdm4febgd5qmzemdu91ri6w";
        for(TeamUser team : teamUserRepository.findByUser(User.builder().id(userId).build())){
            log.info("===== 팀 이름 = {} =====",team.getTeam().getDisplayName());
        }

    }

    @Transactional
    @Test

    void registerTeamUserFromMattermost() {
    }
}