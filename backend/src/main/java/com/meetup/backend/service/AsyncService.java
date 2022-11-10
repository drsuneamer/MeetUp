package com.meetup.backend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * created by seongmin on 2022/11/10
 */
@Service
public class AsyncService {
    @Async
    public void run(Runnable runnable) {
        runnable.run();
    }
}
