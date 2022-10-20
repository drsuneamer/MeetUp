package com.meetup.backend.repository;

import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by seongmin on 2022/10/20
 */
public interface UserRepository extends JpaRepository<User, String> {
}
