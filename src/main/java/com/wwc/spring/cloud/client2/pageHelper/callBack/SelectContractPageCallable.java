package com.wwc.spring.cloud.client2.pageHelper.callBack;

import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.pageHelper.MutilThreadPageCallback;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;

@Component
public class SelectContractPageCallable extends MutilThreadPageCallback{
	
	@Resource(name=BeanNameConsatnt.THREAD_POOL_BEAN)
	private ThreadPoolExecutor executor;

	@Override
	public void excuteByMainThreadAfter(PageQueryExcuteParam sonThreadParam) throws Exception {
		// TODO Auto-generated method stub
//		logger.info("-----------------------------size==="+sonThreadParam.getIdList().size());
	}

	@Override
	public PageQueryExcuteParam excute(PageQueryExcuteParam sonThreadParam) throws Exception {
		Thread.currentThread().sleep(2000);
		for (Long contractIndex : sonThreadParam.getIdList()) {
//			logger.info("-----------------------------id==="+contractIndex);
		}
		return sonThreadParam;
	}

	@Override
	public ThreadPoolExecutor getThreadPoolExecutor() {
		// TODO Auto-generated method stub
		return this.executor;
	}

}
