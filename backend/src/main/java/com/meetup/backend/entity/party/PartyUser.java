package com.meetup.backend.entity.party;

import com.meetup.backend.entity.BaseEntity;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.util.converter.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * created by myeongseok on 2022/10/25
 * updated by seongmin on 2022/11/08
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PartyUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean leader;

    @Builder
    public PartyUser(User user, Party party, boolean leader) {
        this.user = user;
        this.party = party;
        this.leader = leader;
    }

    public void setParty(Party party) {
        if (this.party != null) {
            this.party.getPartyUsers().remove(this);
        }
        this.party = party;
        party.getPartyUsers().add(this);
    }
}
