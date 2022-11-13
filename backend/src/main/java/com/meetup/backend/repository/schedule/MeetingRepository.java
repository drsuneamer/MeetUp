package com.meetup.backend.repository.schedule;

import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.schedule.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * created by seongmin on 2022/10/21
 * updated by seongmin on 2022/10/31
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByMeetup(Meetup meetup);

    List<Meeting> findByParty(Party party);
}
