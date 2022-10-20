package com.meetup.backend.entity.meetup;

import com.meetup.backend.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * created by seongmin on 2022/10/20
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meetup extends BaseEntity {

    @Id
    private String id;

    private String url;

    private String title;

    private String color;

    @Builder
    public Meetup(String id, String url, String title, String color) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.color = color;
    }
}
