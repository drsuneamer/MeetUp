package com.meetup.backend.entity.party;

import com.meetup.backend.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * created by myeongseok on 2022/10/25
 * updated by seonmgin on 2022/11/10
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Party extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "party", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyUser> partyUsers = new ArrayList<>();

    public Party(String name) {
        this.name = name;
    }

    public void addPartyUser(PartyUser partyUser) {
        partyUsers.add(partyUser);
        if (partyUser.getParty() != this) {
            partyUser.setParty(this);
        }
    }
}
