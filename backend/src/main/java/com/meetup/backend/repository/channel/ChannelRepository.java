package com.meetup.backend.repository.channel;

import com.meetup.backend.entity.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/21
 * udpated by seungyong on 2022/11/08
 */
public interface ChannelRepository extends JpaRepository<Channel, String> {

    boolean existsByName(String name);


}
