package com.meetup.backend.repository.channel;

import com.meetup.backend.entity.channel.Channel;
import com.meetup.backend.entity.channel.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by seongmin on 2022/10/21
 * updated by seungyong on 2022/10/23
 */
@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {

    List<Channel> findByChannel(Channel channel);

}
