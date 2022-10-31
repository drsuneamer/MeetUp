package com.meetup.backend.repository.meetup;

import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * created by seongmin on 2022/10/21
 */
public interface MeetupRepository extends JpaRepository<Meetup, Long> {
    List<Meetup> findByManage(User user);
}
