package com.meetup.backend.entity.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    Open("O"),Invite("I");

    private final String code;

    public static RoleType of(String code) {
        for (RoleType type : RoleType.values()) {
            if(type.getCode().equals(code)) {
                return type;
            }
        }
        return RoleType.Open;
    }
}
