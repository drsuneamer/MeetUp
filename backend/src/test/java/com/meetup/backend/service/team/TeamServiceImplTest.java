package com.meetup.backend.service.team;

import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.repository.team.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class TeamServiceImplTest {

    @Autowired
    private TeamRepository teamRepository;

    @Transactional
    @Test
    @DisplayName("팀 저장 테스트")
    void registerTeamFromMattermost() {

        teamRepository.save(Team.builder()
                        .id("팀ID1")
                        .type(TeamType.Open)
                        .name("A102")
                        .displayName("A102채널")
                .build());

        Team team=teamRepository.findById("팀ID1").orElseThrow();

        assertEquals(team.getDisplayName(),"A102채널");
        assertEquals(team.getName(),"A102");
        assertEquals(team.getType(),TeamType.Open);
    }
}