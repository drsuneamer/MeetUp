package com.meetup.backend.repository.team;

import com.meetup.backend.entity.team.Team;
import com.meetup.backend.entity.team.TeamUser;
import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * created by seongmin on 2022/10/21
 * updated by seungyong on 2022/10/22
 * updated by seungyong on 2022/10/25
 */
public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {

    List<TeamUser> findByUser(User user);

    Optional<TeamUser> findByUserAndTeam(User user, Team team);

}
