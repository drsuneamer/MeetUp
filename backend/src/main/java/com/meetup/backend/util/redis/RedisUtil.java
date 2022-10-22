package com.meetup.backend.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * created by seongmin on 2022/10/23
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value, Long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
