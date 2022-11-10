package com.meetup.backend.service.party;

import com.meetup.backend.dto.party.PartyRequestDto;
import com.meetup.backend.dto.party.PartyResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.party.PartyUser;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.repository.party.PartyRepository;
import com.meetup.backend.repository.party.PartyUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * created by seongmin on 2022/11/10
 */
@SpringBootTest
@Slf4j
class PartyServiceImplTest {

    @Autowired
    private PartyService partyService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private PartyUserRepository partyUserRepository;

    private Party party1;

    @BeforeEach
    void before() {
        User user1 = userRepository.save(User.builder()
                .id("user1")
                .nickname("기영이")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Student)
                .build());
        User user2 = userRepository.save(User.builder()
                .id("user2")
                .nickname("기철이")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Student)
                .build());
        User user3 = userRepository.save(User.builder()
                .id("user3")
                .nickname("훈이")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Student)
                .build());
        User user4 = userRepository.save(User.builder()
                .id("user4")
                .nickname("맹구")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Student)
                .build());

        party1 = new Party("첫번째 그룹");
        party1.addPartyUser(new PartyUser(user1, party1, true));
        party1.addPartyUser(new PartyUser(user2, party1, false));
        party1.addPartyUser(new PartyUser(user3, party1, false));

        Party party2 = new Party("두번째 그룹");
        party2.addPartyUser(new PartyUser(user1, party2, false));
        party2.addPartyUser(new PartyUser(user2, party2, true));
        party2.addPartyUser(new PartyUser(user3, party2, false));
        party2.addPartyUser(new PartyUser(user4, party2, false));

        partyRepository.save(party1);
        partyRepository.save(party2);
    }

    @Test
    @DisplayName("그룹 생성")
    void createParty() {
        User user1 = userRepository.findById("user1").get();
        List<String> members = new ArrayList<>();
        members.add("user2");
        members.add("user3");
        Long group1 = partyService.createParty(user1.getId(), new PartyRequestDto("그룹1", members));

        Party party = partyRepository.findById(group1).get();
        assertThat(party.getName()).isEqualTo("그룹1");
        assertThat(partyUserRepository.findByParty(party).size()).isEqualTo(3);
        assertThat(partyUserRepository.findByUserAndParty(user1, party).get().isLeader()).isTrue();
        assertThat(partyUserRepository.findByUserAndParty(userRepository.findById("user2").get(), party).get().isLeader()).isFalse();
    }

    @Test
    @DisplayName("내 그룹 목록 조회")
    void getMyParty() {
        User user1 = userRepository.findById("user1").get();
        List<PartyResponseDto> myParty = partyService.getMyParty(user1.getId());

        assertThat(myParty.size()).isEqualTo(2);
        assertThat(myParty.stream().map(PartyResponseDto::getName).collect(Collectors.toList())).contains("첫번째 그룹").contains("두번째 그룹");
    }

    @Test
    @DisplayName("나를 제외한 그룹 멤버 조회")
    void getPartyMembers() {
        User user1 = userRepository.findById("user1").get();
        List<UserInfoDto> partyMembers = partyService.getPartyMembers(user1.getId(), party1.getId());

        assertThat(partyMembers.size()).isEqualTo(2);
        assertThat(partyMembers.stream().map(UserInfoDto::getId).collect(Collectors.toList())).contains("user2").contains("user3");
    }
}