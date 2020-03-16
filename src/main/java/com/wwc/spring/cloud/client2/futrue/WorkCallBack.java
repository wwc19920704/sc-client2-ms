package com.wwc.spring.cloud.client2.futrue;

import java.util.concurrent.Callable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.service.Impl.WorkServiceImpl;
import com.wwc.spring.cloud.client2.utils.SnowGenerateService;

@Component
public class WorkCallBack implements Callable<Long>{
	
	@Resource(name=BeanNameConsatnt.SNOW_GENERATE_SERVICE_BEAN)
	private SnowGenerateService snowGenerateService;
	
	protected static Logger logger=LoggerFactory.getLogger(WorkCallBack.class);

	@Override
	public Long call() throws Exception {
//		Thread.currentThread().sleep(5000);
		Long b=snowGenerateService.gennerateId();
		logger.info("-----结束b===="+b);
		return b;
	}

}
