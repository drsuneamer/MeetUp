package com.meetup.backend.dto.party;

import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.party.PartyUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by seongmin on 2022/11/10
 */
@AllArgsConstructor
@Getter
public class PartyResponseDto {
    private Long id;
    private String name;
    private boolean leader;
    // 그룹 색?

    public static PartyResponseDto of(PartyUser partyUser) {
        return new PartyResponseDto(partyUser.getParty().getId(), partyUser.getParty().getName(), partyUser.isLeader());

    }
}
