package com.meetup.backend.repository.team;

import com.meetup.backend.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * created by seongmin on 2022/10/21
 * updated by seungyong on 2022/10/22
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    Optional<Team> findById(String id);

}
