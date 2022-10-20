package com.meetup.backend.entity.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by myeongseok on 2022/10/20
 */
@Getter
@AllArgsConstructor
public enum Type {
    Open("O"), Private("P"), Direct("D"), Group("G");

    private final String code;

    public static Type of(String code) {
        for (Type type : Type.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return Type.Private;
    }

}
