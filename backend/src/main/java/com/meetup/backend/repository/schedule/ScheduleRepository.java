package com.meetup.backend.repository.schedule;

import com.meetup.backend.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by myeongseok on 2022/10/25
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}