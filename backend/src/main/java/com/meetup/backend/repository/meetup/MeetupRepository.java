package com.meetup.backend.repository.meetup;

import com.meetup.backend.entity.meetup.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/21
 */
public interface MeetupRepository extends JpaRepository<Meetup, Long> {
}
