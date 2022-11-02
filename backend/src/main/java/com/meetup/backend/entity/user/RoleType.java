package com.meetup.backend.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by seongmin on 2022/10/20
 * updated by seongmin on 2022/10/31
 */
@Getter
@AllArgsConstructor
public enum RoleType {
    ROLE_Consultant("CON"), ROLE_Coach("CO"), ROLE_Pro("P"), ROLE_Student("S"), ROLE_Professor("PF"), ROLE_Admin("A");

    private final String code;

    public static RoleType of(String code) {
        for (RoleType type : RoleType.values()) {
            if(type.getCode().equals(code)) {
                return type;
            }
        }
        return RoleType.ROLE_Student;
    }
}