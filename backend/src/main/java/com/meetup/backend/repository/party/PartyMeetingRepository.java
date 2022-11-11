package com.meetup.backend.repository.party;

import com.meetup.backend.entity.party.PartyMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/11/08
 */
public interface PartyMeetingRepository extends JpaRepository<PartyMeeting, Long> {
}
