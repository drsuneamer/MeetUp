package com.meetup.backend.repository.channel;

import com.meetup.backend.entity.channel.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/21
 */
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {
}
