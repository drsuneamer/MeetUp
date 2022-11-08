package com.meetup.backend.repository.channel;

import com.meetup.backend.entity.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * created by seongmin on 2022/10/21
 * updated by seungyong on 2022/10/23
 * udpated by seungyong on 2022/10/27
 */
public interface ChannelRepository extends JpaRepository<Channel, String> {

    Optional<Channel> findByName(String name);


}
