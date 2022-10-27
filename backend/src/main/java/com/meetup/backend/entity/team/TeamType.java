package com.meetup.backend.entity.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamType {
    Open("O"), Invite("I");

    private final String code;

    public static TeamType of(String code) {
        for (TeamType type : TeamType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return TeamType.Open;
    }
}
