package com.meetup.backend.repository.team;

import com.meetup.backend.entity.team.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/21
 */
public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
}
