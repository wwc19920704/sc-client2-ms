package com.wwc.spring.cloud.client2.config;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;

@Configuration
public class ThreadPoolConfig {

	private int initThreads=10;
	
	private int maxThreads=50;
	
	private int keepAliveTime=0;
	
	private TimeUnit timeUnit=TimeUnit.SECONDS;
	
	@Bean(BeanNameConsatnt.THREAD_POOL_BEAN)
	public ThreadPoolExecutor threadPoolExecutor() {
		return new ThreadPoolExecutor(initThreads, maxThreads, keepAliveTime, timeUnit, new LinkedBlockingQueue<>());
	}
	
	@Bean(BeanNameConsatnt.THREAD_POOL_FOR_BEAN)
	public ThreadPoolExecutor threadPoolExecutorFor() {
		return new ThreadPoolExecutor(initThreads, maxThreads, keepAliveTime, timeUnit, new LinkedBlockingQueue<>());
	}
	
	@Bean("threadPoolExecutorForSpring")
    public ThreadPoolTaskExecutor repayApplyExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        threadPoolTaskExecutor.setMaxPoolSize(50);
        threadPoolTaskExecutor.setQueueCapacity(500);
        threadPoolTaskExecutor.setThreadNamePrefix("threadPoolExecutorForSpring-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
