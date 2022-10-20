package com.meetup.backend.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by seongmin on 2022/10/20
 */
@Getter
@AllArgsConstructor
public enum RoleType {
    Consultant("CON"), Coach("CO"), Pro("P"), Student("S"), Professor("PF");

    private final String code;

    public static RoleType of(String code) {
        for (RoleType type : RoleType.values()) {
            if(type.getCode().equals(code)) {
                return type;
            }
        }
        return RoleType.Student;
    }
}