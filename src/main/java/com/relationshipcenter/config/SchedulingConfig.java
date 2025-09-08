package com.relationshipcenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@Profile("!unitTest")
public class SchedulingConfig {

    @Bean("virtualThreadScheduler")
    public Executor virtualThreadScheduler() {
        return Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    }
}
