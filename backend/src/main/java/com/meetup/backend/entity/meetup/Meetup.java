package com.meetup.backend.entity.meetup;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.user.User;
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
public class Meetup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String color;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Builder
    public Meetup(String title, String color) {
        this.title = title;
        this.color = color;
    }
}
