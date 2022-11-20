package com.meetup.backend.repository.meetup;

import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.meetup.Meetup;
import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * created by seongmin on 2022/10/21
 * updated by seungyong on 2022/10/31
 */
public interface MeetupRepository extends JpaRepository<Meetup, Long> {

    List<Meetup> findByManager(User manager);

    List<Meetup> findByChannelIn(List<Channel> channelList);

    boolean existsByManagerAndChannel(User manager, Channel channel);

    Optional<Meetup> findByManagerAndChannel(User manager, Channel channel);

}
