package com.meetup.backend.repository.meeting;

import com.meetup.backend.entity.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/21
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
