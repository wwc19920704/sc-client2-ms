package com.wwc.spring.cloud.client2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wwc.spring.cloud.client2.constant.BeanNameConsatnt;
import com.wwc.spring.cloud.client2.dto.SelectContractDto;
import com.wwc.spring.cloud.client2.futrue.WorkCallBack;
import com.wwc.spring.cloud.client2.pageHelper.PageUtils;
import com.wwc.spring.cloud.client2.pageHelper.callBack.SelectContractCallback;
import com.wwc.spring.cloud.client2.pageHelper.callBack.SelectContractPageCallable;
import com.wwc.spring.cloud.client2.service.ProductLoanContractService;
import com.wwc.spring.cloud.client2.service.WorkService;

public class FutureTest extends BaseTest{

	@Resource(name=BeanNameConsatnt.THREAD_POOL_BEAN)
	private ThreadPoolExecutor threadPoolExecutor;
	
	@Autowired
	private WorkCallBack workCallBack;
	
	@Autowired
	private WorkService workService;
	
	private HashSet<Long> set=new HashSet<>();
	
	/**
	 * jdk自带的future模式
	 *	组件: 一个实现CallAble<T>接口的实现,实现call方法并返回T的对象,
	 *	          多个FutrueTask类型对象(具体数量是:多少个线程执行就多少个FutrueTask)
	 *	        一个线程安全的队列,用于存放	FutrueTask对象
	 * @throws InterruptedException 
	 */
	@Test
	public void testFutureOfJuc() throws InterruptedException {
		//java中存在两种线程安全的queue,可以分为阻塞队列和非阻塞队列，其中阻塞队列的典型例子是BlockingQueue，非阻塞队列的典型例子是ConcurrentLinkedQueue
//		BlockingQueue<FutureTask<Long>> queue=new ArrayBlockingQueue<>(10000,true);
		ConcurrentLinkedQueue<FutureTask<Long>> queue=new ConcurrentLinkedQueue<>();
		for (int i = 0; i < 10; i++) {
//			WorkCallBack workCallBack=new WorkCallBack();
			FutureTask<Long> futureTask=new FutureTask<>(workCallBack);
			threadPoolExecutor.execute(futureTask);
//			while(!queue.offer(futureTask)) {
//				queue.offer(futureTask);
//			}
			
			while(!queue.add(futureTask)) {
				queue.add(futureTask);
			}
		}
		threadPoolExecutor.shutdown();
		logger.info("---------size-------"+queue.size());
		FutureTask<Long> result=null;
		//去除所有放进去的task
		while((result=queue.peek())!=null) {
			Thread.currentThread().sleep(1000);
			boolean isDone=result.isDone();
			logger.info("-----isDone-----"+isDone);
			if(isDone) {
//				queue.take();
				queue.poll();
			}
		}
	}
	
	/**
	 * springboot集成的多线程任务
	 *       组件:1.被调用的方法上必须加上@Aync注解,另外该方法必须返回org.springframework.util.concurrent.ListenableFuture<T>的实现类的对象,(典型的:org.springframework.scheduling.annotation.AsyncResult<V>)
	 *    2.  一个线程安全的队列,用于存放ListenableFuture<T>的实现类的对象
	 * @throws Exception 
	 *     	获取token的线程睡眠时间1ms
	 *  	试验记录 :
	 *  		 	1000个无相同 *  1 次 
	 *      		10000个无相同 *  1次
	 *      		100000个无相同 *  次
	 * 
	 */
	@Test
	public void testFutureOfSpringBoot() throws Exception {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startDate=new Date();
		logger.info("任务开始--------"+dateFormat.format(new Date()));
		/**
		 * asyncResult
		 */
		ConcurrentLinkedQueue<Future<Long>> queue=new ConcurrentLinkedQueue<>();
		for (int i = 0; i < 100; i++) {
			Future<Long> future=workService.workA();
			while(!queue.add(future)) {
				queue.add(future);
			}
		}
		Future<Long> result=null;
		//去除所有放进去的task
		while((result=queue.peek())!=null) {
//			Thread.currentThread().sleep(1000);
			boolean isDone=result.isDone();
//			logger.info("-----isDone-----"+isDone);
			if(isDone) {
				set.add(result.get());
//				logger.info("-------------value==="+result.get());
				queue.poll();
			}
		}
		logger.info("-----set size-----"+set.size());
		set.clear();
		logger.info("-----set size-----"+set.size());
		/**
		 * FutureTask
		 */
		Date endDate=new Date();
		logger.info("任务结束--------------------"+(endDate.getTime()-startDate.getTime()));
	}
	
	@Autowired
	private  ProductLoanContractService productLoanContractService;
	
	@Autowired
	private SelectContractPageCallable selectContractPageCallable;
	
	@Autowired
	private SelectContractCallback selectContractCallback;
	
	/**
	 * 多线程分页查询
	 * @throws Exception
	 */
	@Test
	public void testPageUtilsThread() throws Exception {
		//组装分页查询条件
		SelectContractDto selectContractDto=new SelectContractDto();
		selectContractDto.setNeedObjList(false);
		selectContractDto.setPrimaryDifference(selectContractDto.ONE_HUNDRED*selectContractDto.ONE_HUNDRED);
		selectContractDto.setQueryLimitCustomize(selectContractDto.ONE_HUNDRED);
//		selectContractDto.setQueryTimes(10);
		//多线程查询
		PageUtils.queryExcuteByMutilThreads(productLoanContractService, selectContractDto, selectContractPageCallable, null);
		//单线程分页查询
//		PageUtils.queryExcute(productLoanContractService, selectContractDto, selectContractCallback, null);
	}
}
