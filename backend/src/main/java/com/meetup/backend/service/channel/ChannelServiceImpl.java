package com.meetup.backend.service.channel;

import com.meetup.backend.dto.channel.ChannelResponseDto;
import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.team.Team;
import com.meetup.backend.repository.channel.ChannelRepository;
import com.meetup.backend.repository.team.TeamRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * created by myeongseok on 2022/10/21
 * updated by seungyong on 2022/10/23
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;

    private final TeamRepository teamRepository;

    @Override
    public List<ChannelResponseDto> getChannelByTeam(String teamId) {

        Team team=teamRepository.findById(teamId).orElseThrow(() -> new BadRequestException("유효하지 않은 팀입니다."));
        List<ChannelResponseDto> channelResponseDtoList =new ArrayList<>();

        for(Channel channel : channelRepository.findByTeam(team)){
            channelResponseDtoList.add(ChannelResponseDto.of(channel));
        }

        return channelResponseDtoList;
    }
}
