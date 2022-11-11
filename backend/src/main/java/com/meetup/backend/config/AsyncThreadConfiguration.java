package com.meetup.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * created by seongmin on 2022/11/10
 */
@Configuration
@EnableAsync
public class AsyncThreadConfiguration {
    @Bean
    public Executor asyncThreadTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setThreadNamePrefix("meet-up-pool");
        return threadPoolTaskExecutor;
    }
}
