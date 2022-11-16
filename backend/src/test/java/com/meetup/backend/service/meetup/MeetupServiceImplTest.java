package com.meetup.backend.service.meetup;

import com.meetup.backend.dto.admin.ChangeRoleDto;
import com.meetup.backend.dto.meetup.MeetupRequestDto;
import com.meetup.backend.dto.meetup.MeetupUpdateRequestDto;
import com.meetup.backend.dto.user.LoginRequestDto;
import com.meetup.backend.dto.user.LoginResponseDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.admin.AdminService;
import com.meetup.backend.service.channel.ChannelUserService;
import com.meetup.backend.service.user.UserService;
import com.meetup.backend.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class MeetupServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MeetupService meetupService;

    @Autowired
    private ChannelUserService channelUserService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelUserRepository channelUserRepository;

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${mattermost.id}")
    private String id;

    @Value("${mattermost.password}")
    private String password;

    private String mmId;

    private String mmSessionToken;

    private String adminId;

    @BeforeEach
    void Before() {

        LoginResponseDto loginResponse = userService.login(new LoginRequestDto(id, password));
        mmId = loginResponse.getId();
        mmSessionToken = redisUtil.getData(mmId);

    }

    @AfterEach
    void After() {
        meetupRepository.deleteAll();
        channelUserRepository.deleteAll();
        channelRepository.deleteAll();
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();

        redisUtil.deleteData(mmId);
    }

    @Test
    @DisplayName("밋업 생성")
    @Order(1)
    void registerMeetUp() {

        adminId = "admin";
        adminService.signUp(adminId, adminId);
        List<ChangeRoleDto> changeRoleDtoList = new ArrayList<>();
        changeRoleDtoList.add(ChangeRoleDto.builder().id(mmId).roleType(RoleType.ROLE_Consultant).build());
        adminService.changeRole(adminId, changeRoleDtoList);

        MeetupRequestDto meetupRequestDto = MeetupRequestDto.builder()
                .channelId("zfxq9ebfu7nsmya3gxdat9i6yh")
                .color("color1")
                .title("title1")
                .build();
        Meetup meetupEntity = new Meetup(meetupRequestDto.getTitle(), meetupRequestDto.getColor(),
                User.builder().id(mmId).build(),
                Channel.builder().id(meetupRequestDto.getChannelId()).build());

        meetupService.registerMeetUp(meetupRequestDto, mmId);

        Meetup meetup = meetupRepository.findByManager(User.builder().id(mmId).build()).get(0);
        assertThat(meetup.getManager().getId()).isEqualTo(mmId);
        assertThat(meetup.getChannel().getId()).isEqualTo(meetupRequestDto.getChannelId());
        assertThat(meetup.getColor()).isEqualTo(meetupRequestDto.getColor());
        assertThat(meetup.getTitle()).isEqualTo(meetupRequestDto.getTitle());

    }

    @Test
    @Order(2)
    @DisplayName("밋업 변경")
    void updateMeetup() {

        registerMeetUp();

        MeetupUpdateRequestDto meetupUpdateRequestDto = MeetupUpdateRequestDto.builder()
                .color("color2")
                .title("title2")
                .build();
        meetupService.updateMeetup(meetupUpdateRequestDto, mmId, meetupRepository.findByManager(User.builder().id(mmId).build()).get(0).getId());
        Meetup meetup = meetupRepository.findByManager(User.builder().id(mmId).build()).get(0);
        assertThat(meetup.getColor()).isEqualTo(meetupUpdateRequestDto.getColor());
        assertThat(meetup.getTitle()).isEqualTo(meetupUpdateRequestDto.getTitle());

    }

    @Test
    @Order(3)
    @DisplayName("밋업목록 보기")
    void getResponseDtos() {

        registerMeetUp();

        log.info(meetupService.getResponseDtoList(mmId) + "");
        assertThat(meetupService.getResponseDtoList(mmId)).isNotEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("밋업 정보 보기")
    void getMeetupInfo() {

        registerMeetUp();

        log.info(meetupService.getMeetupInfo(meetupRepository.findByManager(User.builder().id(mmId).build()).get(0).getId()) + "");
        assertThat(meetupService.getMeetupInfo(meetupRepository.findByManager(User.builder().id(mmId).build()).get(0).getId())).isNotNull();
    }

    @Test
    @Order(5)
    @DisplayName("달력 목록 보기")
    void getCalendarList() {

        registerMeetUp();

        assertThat(meetupService.getCalendarList("syngmnbiytd8ugp5834doy1gme", channelUserService.getChannelUserByUser("syngmnbiytd8ugp5834doy1gme")).size()).isNotSameAs(0);
    }

    @Test
    @DisplayName("밋업 삭제")
    @Order(6)
    void deleteMeetup() {

        registerMeetUp();

        meetupService.deleteMeetup(meetupRepository.findByManager(User.builder().id(mmId).build()).get(0).getId(), mmId);
        assertThat(meetupRepository.findByManager(User.builder().id(mmId).build()).get(0).isDelete()).isSameAs(true);
    }
}