package com.wwc.spring.cloud.client2.service.Impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureTask;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.futrue.WorkCallBack;
import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;
import com.wwc.spring.cloud.client2.pageHelper.callBack.SelectContractCallback;
import com.wwc.spring.cloud.client2.service.ProductLoanContractService;
import com.wwc.spring.cloud.client2.service.WorkService;

@Service
public class WorkServiceImpl implements WorkService{
	
	protected static Logger logger=LoggerFactory.getLogger(WorkServiceImpl.class);
	
	@Autowired
	private WorkCallBack workCallBack;
	
	@Resource(type=SelectContractCallback.class)
	private PageCallBack selectContractCallback;
	
	@Autowired
	private ProductLoanContractService productLoanContractService;

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
