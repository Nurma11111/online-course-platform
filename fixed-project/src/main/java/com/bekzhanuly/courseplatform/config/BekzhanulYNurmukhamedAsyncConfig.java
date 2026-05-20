package com.bekzhanuly.courseplatform.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Async Thread Pool Configuration
 * Configures the executor used by all @Async methods
 * Author: Bekzhanuly Nurmukhamed
 */
@Configuration
@Slf4j
public class BekzhanulYNurmukhamedAsyncConfig implements AsyncConfigurer {

    @Value("${async.core-pool-size:5}")
    private int corePoolSize;

    @Value("${async.max-pool-size:10}")
    private int maxPoolSize;

    @Value("${async.queue-capacity:100}")
    private int queueCapacity;

    @Value("${async.thread-name-prefix:BekzhanulуAsync-}")
    private String threadNamePrefix;

    @Bean(name = "bekzhanulYAsyncExecutor")
    public Executor bekzhanulYAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler((r, exec) ->
            log.error("[ASYNC] Task rejected — thread pool is full! Task: {}", r.toString())
        );
        executor.initialize();
        log.info("Async executor initialized: corePool={}, maxPool={}, queue={}",
                corePoolSize, maxPoolSize, queueCapacity);
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return bekzhanulYAsyncExecutor();
    }
}
