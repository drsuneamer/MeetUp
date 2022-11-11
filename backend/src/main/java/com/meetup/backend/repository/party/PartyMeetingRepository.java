package com.meetup.backend.repository.party;

import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.party.PartyMeeting;
import com.meetup.backend.entity.schedule.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * created by seongmin on 2022/11/08
 * updated by myeongseok on 2022/11/11
 */
public interface PartyMeetingRepository extends JpaRepository<PartyMeeting, Long> {

    boolean existsByParty(Party party);

    boolean existsByMeeting(Meeting meeting);

    List<PartyMeeting> findByParty(Party party);

    List<PartyMeeting> findByMeeting(Meeting meeting);
}
