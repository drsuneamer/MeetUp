package com.meetup.backend.repository.schedule;

import com.meetup.backend.entity.schedule.Schedule;
import com.meetup.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by myeongseok on 2022/10/25
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.user = :user AND s.type = 'schedule' AND s.start BETWEEN :from AND :to ")
    List<Schedule> findAllByStartBetweenAndUser(LocalDateTime from, LocalDateTime to, User user);
}