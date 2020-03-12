package com.wwc.spring.cloud.client2.futrue;

import java.util.concurrent.Callable;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.utils.SnowGenerateService;

@Component
public class WorkCallBack implements Callable<Long>{
	
	@Resource(name=BeanNameConsatnt.SNOW_GENERATE_SERVICE_BEAN)
	private SnowGenerateService snowGenerateService;

	@Override
	public Long call() throws Exception {
//		Thread.currentThread().sleep(5000);
		return snowGenerateService.gennerateId();
	}

}
