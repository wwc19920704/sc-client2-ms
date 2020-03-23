package com.wwc.spring.cloud.client2.pageHelper;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.EnableAsync;

import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;


/**
 * 	专门为spring框架而准备的多线程回调函数
 * @author wwc
 *
 */
//开启异步方法
@EnableAsync
public  abstract class MutilThreadPageCallbackForSpring extends PageCallBack{

	
	/**
	 * 设置queue
	 */
	public MutilThreadPageCallbackForSpring() {
		//初始化队列
		initQueue();
	}
	
	
	/**
	 * 线程处理生成的future队列
	 */
	private ConcurrentLinkedQueue<Future<PageQueryExcuteParam>> futureQueue;
	
	/**
	 * 初始化参数队列和future对象队列
	 */
	public void initQueue() {
		// TODO Auto-generated method stub
		this.futureQueue=new ConcurrentLinkedQueue<Future<PageQueryExcuteParam>>();
	}
	public ConcurrentLinkedQueue<Future<PageQueryExcuteParam>> getFutureQueue() {
		return futureQueue;
	}
	
	public PageQueryExcuteParam excute(PageQueryExcuteParam sonThreadParam) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 	子线程执行的方法  必须加上@Async()注解,同时表明线程池的名称
	 * @param sonThreadParam
	 * @return
	 * @throws Exception
	 */
	public abstract Future<PageQueryExcuteParam> excuteBySonThread(PageQueryExcuteParam sonThreadParam) throws Exception;
}
