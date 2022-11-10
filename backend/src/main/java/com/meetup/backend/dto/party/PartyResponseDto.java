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
    // 그룹 색?

    public static PartyResponseDto of(Party party) {
        return new PartyResponseDto(party.getId(), party.getName());

    }
}
