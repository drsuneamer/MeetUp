package com.meetup.backend.repository.party;

import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.party.PartyUser;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.meetup.backend.entity.user.RoleType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * created by seongmin on 2022/11/08
 */
@SpringBootTest
@Slf4j
class PartyUserRepositoryTest {

    @Autowired
    private PartyUserRepository partyUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Test
    @DisplayName("중복 그룹 이름 확인")
    void duplicateGroupName() {
        User user = userRepository.save(new User("id", "pwd", "홍길동", "webex.com", ROLE_Student, true));
        User member1 = userRepository.save(new User("memberId", "pwd", "그룹원1", "webex.com", ROLE_Student, true));
        Party party = partyRepository.save(new Party("그룹1"));
        PartyUser party1 = partyUserRepository.save(new PartyUser(user, party, true));
        partyUserRepository.save(new PartyUser(member1, party, false));

        assertThat(partyUserRepository.existByGroupName(user, true, "그룹1")).isTrue();
        assertThat(partyUserRepository.existByGroupName(user, true, "그룹2")).isFalse();
        assertThat(partyUserRepository.existByGroupName(member1, true, "그룹1")).isFalse();
    }
}