package com.meetup.backend.repository.user;

import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * created by seongmin on 2022/10/20
 * updated by seongmin on 2022/10/31
 */
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByFirstLogin(boolean isFirstLogin);
}
