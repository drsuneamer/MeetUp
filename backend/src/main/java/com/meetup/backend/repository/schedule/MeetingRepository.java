package com.meetup.backend.repository.schedule;

import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.schedule.Meeting;
import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by seongmin on 2022/10/21
 * updated by seongmin on 2022/11/14
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByMeetup(Meetup meetup);

    List<Meeting> findByParty(Party party);

    @Query("SELECT m FROM Meeting m WHERE m.party = :party AND m.start BETWEEN :from AND :to ")
    List<Meeting> findAllByStartBetweenAndParty(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("party") Party party);
}
