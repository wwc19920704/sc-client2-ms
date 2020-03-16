package com.wwc.spring.cloud.client2.pageHelper.callBack;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.pageHelper.PageCallBackByMutilThread;
import com.wwc.spring.cloud.client2.service.WorkService;

@Component
public class MutilThreadSelectContractCallback extends PageCallBackByMutilThread{
	
	@Autowired
	private WorkService workService;

	@Override
	@Async(value=BeanNameConsatnt.THREAD_POOL_BEAN)
	public Future excuteBySonThread(List result, List<Long> ids) throws Exception {
		Thread.currentThread().sleep(2000);
		for (Long contractIndex : ids) {
			logger.info("-----------------------------id==="+contractIndex);
		}
		return new AsyncResult<>("");
	}

	@Override
	public void excuteByMainThreadAfter(List result, List ids, Object ret) throws Exception {
		// TODO Auto-generated method stub
		logger.info("处理了=="+ids.size()+"条数据");
	}
}
