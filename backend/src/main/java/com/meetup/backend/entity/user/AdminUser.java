package com.meetup.backend.entity.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * created by myeongseok on 2022.10.20
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "ADMINUSERS")
public class AdminUser {
    @Id
    private String id;

    private String password;
}
