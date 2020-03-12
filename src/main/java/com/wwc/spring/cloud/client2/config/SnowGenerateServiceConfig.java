package com.wwc.spring.cloud.client2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.utils.SnowGenerateService;

@Configuration
public class SnowGenerateServiceConfig {

	/**
	 * 初始化一个雪花算法服务
	 * @return
	 */
	@Bean(BeanNameConsatnt.SNOW_GENERATE_SERVICE_BEAN)
	public SnowGenerateService snowGenerateService() {
		return new SnowGenerateService();
	}
}
