package com.wwc.spring.cloud.client2.pageHelper.callBack;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.pageHelper.MutilThreadPageCallbackForSpring;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;

@Component
public class SelectContractPageCallableForSpring extends MutilThreadPageCallbackForSpring{

	@Resource(name=BeanNameConsatnt.THREAD_POOL_BEAN)
	private ThreadPoolTaskExecutor executor;
	
	@Override
	public Future<PageQueryExcuteParam> excuteBySonThread(PageQueryExcuteParam sonThreadParam) throws Exception {
		for (Long contractIndex : sonThreadParam.getIdList()) {
			logger.info("-----------for------------------id==="+contractIndex);
		}
		return new AsyncResult<PageQueryExcuteParam>(sonThreadParam);
	}

	@Override
	public ThreadPoolTaskExecutor getThreadPoolExecutor() {
		// TODO Auto-generated method stub
		return executor;
	}

	@Override
	public int getQueueMaxsize() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public void excuteByMainThreadAfter(PageQueryExcuteParam sonThreadParam) throws Exception {
		// TODO Auto-generated method stub

	}

}
