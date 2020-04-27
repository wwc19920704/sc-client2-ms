package com.wwc.spring.cloud.client2.pageHelper;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;


/**
 * 	多线程执行的回调主逻辑
 * @author wwc
 *
 */
public abstract class MutilThreadPageCallback extends PageCallBack implements Callable <PageQueryExcuteParam>{

	/**
	 * 设置queue
	 */
	public MutilThreadPageCallback() {
		//初始化队列
		initQueue();
	}


	/**
	 * 	参数队列
	 */
	private ConcurrentLinkedQueue<PageQueryExcuteParam> paramQueue;
	
	/**
	 * 线程处理生成的future队列
	 */
	private ConcurrentLinkedQueue<Future<PageQueryExcuteParam>> futureQueue;
	
	/**
	 * 	获取线程池,必须在实现类中返回一个初始化好的线程池
	 * @return
	 */
	public abstract ThreadPoolExecutor getThreadPoolExecutor();

	public ConcurrentLinkedQueue<Future<PageQueryExcuteParam>> getFutureQueue() {
		return futureQueue;
	}
	
	
	public ConcurrentLinkedQueue<PageQueryExcuteParam> getParamQueue() {
		return paramQueue;
	}

	/**
	 * 初始化参数队列和future对象队列
	 */
	public void initQueue() {
		// TODO Auto-generated method stub
		this.futureQueue=new ConcurrentLinkedQueue<Future<PageQueryExcuteParam>>();
		this.paramQueue=new ConcurrentLinkedQueue<PageQueryExcuteParam>();
	}
	
	/**
	 * call方法调用子类实现
	 */
	@Override
	public PageQueryExcuteParam call() throws Exception {
		//获取参数
		PageQueryExcuteParam param=this.getParamQueue().peek();
		//判断参数对象的处理状态,如果状态为处理中,则需要调用子类的excute方法执行主逻辑
		if(param.getIsProcess()==PageQueryExcuteParam.PROCESSING) {
			//执行之前设置参数对象的处理状态为已处理
			param.setIsProcess(PageQueryExcuteParam.PROCESSED);
			return excute(param);
		}
		return param;
		
	}
}
