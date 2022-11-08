package com.meetup.backend.repository.party;

import com.meetup.backend.entity.party.PartyUser;
import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * created by seongmin on 2022/11/08
 */
public interface PartyUserRepository extends JpaRepository<PartyUser, Long> {

    @Query("select count(p.id) > 0 from Party p left join PartyUser pu on p.id = pu.party.id where pu.user = :user and pu.leader = :leader and p.name = :name")
    boolean existByGroupName(@Param("user") User user, @Param("leader") boolean leader, @Param("name") String name);

}
