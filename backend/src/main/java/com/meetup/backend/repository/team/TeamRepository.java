package com.meetup.backend.repository.team;

import com.meetup.backend.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/21
 */
public interface TeamRepository extends JpaRepository<Team, String> {
}
