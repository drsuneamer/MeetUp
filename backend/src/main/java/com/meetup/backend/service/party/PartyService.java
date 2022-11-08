package com.meetup.backend.service.party;

import com.meetup.backend.dto.party.PartyRequestDto;

import java.util.List;

/**
 * created by seongmin on 2022/11/08
 */
public interface PartyService {
    Long createParty(String userId, PartyRequestDto requestDto);
}
