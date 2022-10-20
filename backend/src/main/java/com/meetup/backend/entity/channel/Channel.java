package com.meetup.backend.entity.channel;

import com.meetup.backend.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "CHANNELS")
public class Channel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String displyName;

    @Enumerated(EnumType.STRING)
    private String type;

    @Builder
    public Channel(String name, String displyName, String type){
        this.name = name;
        this.displyName = displyName;
        this.type = type;
    }
    
}
