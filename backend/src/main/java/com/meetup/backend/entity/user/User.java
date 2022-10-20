package com.meetup.backend.entity.user;

import com.meetup.backend.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * created by seongmin on 2022/10/20
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "USERS")
public class User extends BaseEntity {

    @Id
    private String id;

    private String email;

    private String nickname;

    private String webex;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Builder
    public User(String id, String email, String nickname, String webex, RoleType role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.webex = webex;
        this.role = role;
    }
}
