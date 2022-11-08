package com.meetup.backend.entity.party;

import com.meetup.backend.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * created by myeongseok on 2022/10/25
 * updated by seonmgin on 2022/11/08
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Party extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Party(String name) {
        this.name = name;
    }
}
