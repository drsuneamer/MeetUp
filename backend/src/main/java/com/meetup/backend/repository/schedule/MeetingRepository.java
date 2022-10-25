package com.meetup.backend.repository.schedule;

import com.meetup.backend.entity.schedule.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/21
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
