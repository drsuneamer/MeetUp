package com.meetup.backend.entity.user;

import com.meetup.backend.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * created by seongmin on 2022/10/20
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "USERS")
@ToString
public class User extends BaseEntity {

    @Id
    private String id;

    private String password;

    private String nickname;

    private String webex;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Builder
    public User(String id, String password, String nickname, String webex, RoleType role) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.webex = webex;
        this.role = role;
    }
}
