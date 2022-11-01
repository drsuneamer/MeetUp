package com.meetup.backend.entity.user;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import lombok.*;

import javax.persistence.*;

/**
 * created by seongmin on 2022/10/20
 * updated by seungyong on 2022/10/31
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

    @Convert(converter = BooleanToYNConverter.class)
    private boolean firstLogin;

    public void changePwd(String pwd) {
        this.password = pwd;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public void setFirstLogin() {
        firstLogin = true;
    }

    public void changeRole(RoleType role) {
        this.role = role;
    }

    public void setWebex(String webex) {
        this.webex = webex;
    }

    @Builder
    public User(String id, String password, String nickname, String webex, RoleType role, boolean firstLogin) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.webex = webex;
        this.role = role;
        this.firstLogin = firstLogin;
    }
}
