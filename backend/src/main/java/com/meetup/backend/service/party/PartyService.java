package com.meetup.backend.service.party;

import com.meetup.backend.dto.party.PartyRequestDto;
import com.meetup.backend.dto.party.PartyResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;

import java.util.List;

/**
 * created by seongmin on 2022/11/08
 * updated by seongmin on 2022/11/10
 */
public interface PartyService {
    Long createParty(String userId, PartyRequestDto requestDto);

    List<PartyResponseDto> getMyParty(String userId);

    List<UserInfoDto> getPartyMembers(String userId, Long partyId);

    void deleteParty(String userId, Long partyId);
}
