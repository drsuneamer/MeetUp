package com.meetup.backend.service.meeting;

import com.meetup.backend.dto.schedule.meeting.MeetingRequestDto;
import com.meetup.backend.dto.schedule.meeting.MeetingResponseDto;
import com.meetup.backend.dto.schedule.meeting.MeetingUpdateRequestDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelType;
import com.meetup.backend.entity.channel.ChannelUser;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamType;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.RoleType;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.channel.ChannelUserRepository;
import com.meetup.backend.repository.meetup.MeetupRepository;
import com.meetup.backend.repository.schedule.MeetingRepository;
import com.meetup.backend.repository.team.TeamRepository;
import com.meetup.backend.repository.team.TeamUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
class MeetingServiceImplTest {

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
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Meetup consultantMeetup;
    private Meetup coachMeetup;

    @BeforeEach
    void before() {
        User con1 = userRepository.save(User.builder()
                .id("consultant")
                .nickname("홍사범")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Consultant)
                .build());

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
        User coach1 = userRepository.save(User.builder()
                .id("coach1")
                .nickname("나코치")
                .password(passwordEncoder.encode("password123"))
                .firstLogin(true)
                .role(RoleType.ROLE_Coach)
                .build());


        Team team = teamRepository.save(Team.builder()
                .id("team1")
                .name("1team")
                .displayName("1팀")
                .type(TeamType.Open)
                .build());

        teamUserRepository.save(new TeamUser(team, con1));
        teamUserRepository.save(new TeamUser(team, user1));
        teamUserRepository.save(new TeamUser(team, user2));
        teamUserRepository.save(new TeamUser(team, coach1));

        Channel channel1 = channelRepository.save(Channel.builder()
                .id("channel1")
                .name("1channel")
                .displyName("채널1")
                .team(team)
                .type(ChannelType.Open)
                .build());
        Channel channel2 = channelRepository.save(Channel.builder()
                .id("channel2")
                .name("1channe2")
                .displyName("채널2")
                .team(team)
                .type(ChannelType.Open)
                .build());

        // con1, user1은 channel 1, 2 모두 속함
        // user2는 channel1에만 속함.
        channelUserRepository.save(new ChannelUser(con1, channel1));
        channelUserRepository.save(new ChannelUser(con1, channel2));
        channelUserRepository.save(new ChannelUser(user1, channel1));
        channelUserRepository.save(new ChannelUser(user2, channel1));
        channelUserRepository.save(new ChannelUser(user1, channel2));
        channelUserRepository.save(new ChannelUser(coach1, channel1));
        channelUserRepository.save(new ChannelUser(coach1, channel2));

        consultantMeetup = meetupRepository.save(new Meetup("컨설턴트 미팅 밋업", "AAAAA", con1, channel1));
        coachMeetup = meetupRepository.save(new Meetup("코치 미팅 밋업", "BBBBB", coach1, channel2));


    }

    @AfterEach
    void after() {
        meetingRepository.deleteAll();
        meetupRepository.deleteAll();
        channelUserRepository.deleteAll();
        channelRepository.deleteAll();
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("미팅 아이디로 미팅 상세 조회")
    void getMeetingResponseDtoById() {
        User user1 = userRepository.findById("user1").get();
        User user2 = userRepository.findById("user2").get();
        Long meetingId1 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 10:00:00")
                .end("2022-11-04 11:30:00")
                .title("제목1")
                .content("내용1")
                .meetupId(consultantMeetup.getId())
                .build());
        Long meetingId2 = meetingService.createMeeting(user2.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 16:00:00")
                .end("2022-11-04 16:30:00")
                .title("제목2")
                .content("내용2")
                .meetupId(consultantMeetup.getId())
                .build());
        MeetingResponseDto meetingResponseDto1 = meetingService.getMeetingDetail(user1.getId(), meetingId1);
        assertThat(meetingResponseDto1.getContent()).isEqualTo("내용1");

        MeetingResponseDto meetingResponseDto2 = meetingService.getMeetingDetail(consultantMeetup.getManager().getId(), meetingId1);
        assertThat(meetingResponseDto2.getContent()).isEqualTo("내용1");

        assertThatThrownBy(() -> {
            meetingService.getMeetingDetail(user2.getId(), meetingId1);
        }).isInstanceOf(ApiException.class).hasMessageContaining("권한");

    }


    @Test
    @DisplayName("미팅 등록")
    void createMeeting() {
        User user1 = userRepository.findById("user1").get();
        User user2 = userRepository.findById("user2").get();
        Long meetingId1 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 10:00:00")
                .end("2022-11-04 11:30:00")
                .title("제목1")
                .content("내용1")
                .meetupId(consultantMeetup.getId())
                .build());
        Long meetingId2 = meetingService.createMeeting(user2.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 16:00:00")
                .end("2022-11-04 16:30:00")
                .title("제목2")
                .content("내용2")
                .meetupId(consultantMeetup.getId())
                .build());
        MeetingResponseDto meetingResponseDto1 = meetingService.getMeetingDetail(user1.getId(), meetingId1);
        assertThat(meetingResponseDto1.getContent()).isEqualTo("내용1");

        MeetingResponseDto meetingResponseDto2 = meetingService.getMeetingDetail(consultantMeetup.getManager().getId(), meetingId1);
        assertThat(meetingResponseDto2.getContent()).isEqualTo("내용1");

        MeetingResponseDto meetingResponseDto3 = meetingService.getMeetingDetail(user2.getId(), meetingId2);
        assertThat(meetingResponseDto3.getContent()).isEqualTo("내용2");

    }

    @Test
    @DisplayName("미팅 일정 중복으로 등록 불가")
    void refuseCreateMeeting() {
        User user1 = userRepository.findById("user1").get();
        User user2 = userRepository.findById("user2").get();
        Long meetingId1 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 10:00:00")
                .end("2022-11-04 11:30:00")
                .title("제목1")
                .content("내용1")
                .meetupId(consultantMeetup.getId())
                .build());
        Long meetingId2 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 14:00:00")
                .end("2022-11-04 14:30:00")
                .title("제목1")
                .content("내용1")
                .meetupId(consultantMeetup.getId())
                .build());

        assertThatThrownBy(() -> {
            Long meetingId3 = meetingService.createMeeting(user2.getId(), MeetingRequestDto
                    .builder()
                    .start("2022-11-04 13:50:00")
                    .end("2022-11-04 14:20:00")
                    .title("제목3")
                    .content("내용3")
                    .meetupId(consultantMeetup.getId())
                    .build());
        }).isInstanceOf(ApiException.class).hasMessageContaining("중복");

    }

    @Test
    @DisplayName("미팅 수정")
    void updateMeeting() {
        User user1 = userRepository.findById("user1").get();
        Long meetingId1 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 10:00:00")
                .end("2022-11-04 11:30:00")
                .title("제목1")
                .content("내용1")
                .meetupId(consultantMeetup.getId())
                .build());
        Long meetingId2 = meetingService.updateMeeting(user1.getId(), MeetingUpdateRequestDto
                .builder()
                .id(meetingId1)
                .start("2022-11-04 14:00:00")
                .end("2022-11-04 14:30:00")
                .title("제목2")
                .content("내용 22").
                build());
        MeetingResponseDto meetingResponseDto = meetingService.getMeetingDetail(user1.getId(), meetingId2);
        assertThat(meetingResponseDto.getContent()).isEqualTo("내용 22");
    }

    @Test
    @DisplayName("미팅 일정 중복으로 수정 불가")
    void refuseUpdateMeeting() {
        User user1 = userRepository.findById("user1").get();
        User user2 = userRepository.findById("user2").get();
        Long meetingId1 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 10:00:00")
                .end("2022-11-04 11:30:00")
                .title("제목1")
                .content("내용1")
                .meetupId(consultantMeetup.getId())
                .build());
        Long meetingId2 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 14:00:00")
                .end("2022-11-04 14:30:00")
                .title("제목2")
                .content("내용2")
                .meetupId(consultantMeetup.getId())
                .build());

        assertThatThrownBy(() -> {
            Long meetingId3 = meetingService.updateMeeting(user1.getId(), MeetingUpdateRequestDto
                    .builder()
                    .id(meetingId2)
                    .start("2022-11-04 09:00:00")
                    .end("2022-11-04 11:00:00")
                    .title("제목2")
                    .content("내용 22").
                    build());
        }).isInstanceOf(ApiException.class).hasMessageContaining("중복");
    }

    @Test
    @DisplayName("미팅 삭제")
    void deleteMeeting() {
        User user1 = userRepository.findById("user1").get();
        Long meetingId1 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 10:00:00")
                .end("2022-11-04 11:30:00")
                .title("제목1")
                .content("내용1")
                .meetupId(consultantMeetup.getId())
                .build());
        Long meetingId2 = meetingService.createMeeting(user1.getId(), MeetingRequestDto
                .builder()
                .start("2022-11-04 14:00:00")
                .end("2022-11-04 14:30:00")
                .title("제목2")
                .content("내용2")
                .meetupId(consultantMeetup.getId())
                .build());
        meetingService.deleteMeeting(user1.getId(), meetingId2);

        assertThatThrownBy(() -> {
            meetingService.getMeetingDetail(user1.getId(), meetingId2);
        }).isInstanceOf(ApiException.class).hasMessageContaining("미팅");
    }
}