package com.wwc.spring.cloud.client2.service;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFutureTask;

public interface WorkService {

	
	/**
	 * 	返回ListenableFutureTask对象的多线程方法, 继承自FutureTask<T>同时实现 ListenableFuture<T>
	 * @return
	 */
	public ListenableFutureTask<Long> work();
	
	/**
	 * 	返回AsyncResult对象的多线程方法,实现 ListenableFuture<T>
	 * @return
	 */
	public AsyncResult<Long> workA() throws Exception;
}
