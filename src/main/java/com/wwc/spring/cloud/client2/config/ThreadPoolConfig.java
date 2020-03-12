package com.wwc.spring.cloud.client2.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;

@Configuration
public class ThreadPoolConfig {

	private int initThreads=20;
	
	private int maxThreads=50;
	
	private int keepAliveTime=0;
	
	private TimeUnit timeUnit=TimeUnit.SECONDS;
	
	private BlockingQueue workQueue=new LinkedBlockingQueue<>();
	
	@Bean(BeanNameConsatnt.THREAD_POOL_BEAN)
	public ThreadPoolExecutor threadPoolExecutor() {
		return new ThreadPoolExecutor(initThreads, maxThreads, keepAliveTime, timeUnit, workQueue);
	}
}
