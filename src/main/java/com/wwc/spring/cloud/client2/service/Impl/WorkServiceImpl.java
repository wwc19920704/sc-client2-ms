package com.wwc.spring.cloud.client2.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureTask;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.futrue.WorkCallBack;
import com.wwc.spring.cloud.client2.service.WorkService;
import com.wwc.spring.cloud.client2.utils.SnowGenerateService;

@Service
public class WorkServiceImpl implements WorkService{
	
	@Autowired
	private WorkCallBack workCallBack;

	@Async(value=BeanNameConsatnt.THREAD_POOL_BEAN)
	@Override
	public ListenableFutureTask<Long> work() {
		// TODO Auto-generated method stub
		return new ListenableFutureTask<>(workCallBack);
	}

	@Async(value=BeanNameConsatnt.THREAD_POOL_BEAN)
	@Override
	public AsyncResult<Long> workA() throws Exception {
		// TODO Auto-generated method stub
		return new AsyncResult<>(workCallBack.call()) ;
	}
}
