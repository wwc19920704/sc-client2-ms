package com.wwc.spring.cloud.client2.pageHelper.excute;

import java.util.concurrent.Future;

import com.wwc.spring.cloud.client2.pageHelper.MutilThreadPageCallbackForSpring;
import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;


/**
 * spring框架的多线程处理接口
 * @author wwc
 *
 */
public class MutilThreadExcuteForSpring extends  Excute{

	@Override
	public void excute(PageCallBack pageCallBack, PageQueryExcuteParam excuteParam) throws Exception {
		MutilThreadPageCallbackForSpring callable=(MutilThreadPageCallbackForSpring) pageCallBack;
		//如果线程处理结果的队列的长度大于最大线程长度,或者是future列表长度大于等待队列的长度
		if(callable.getThreadPoolExecutor().getThreadPoolExecutor().getQueue().size()>=callable.getQueueMaxsize()
			| callable.getFutureQueue().size()>=(callable.getQueueMaxsize())) {
			Future result=null;
			//等待移出一个线程,继续执行
			while((result=callable.getFutureQueue().peek())!=null) {
				if(result.isDone()) {
					//移出
					callable.getFutureQueue().poll();
					//就算留出一个空格,也要保证队列中始终有一个空间空着
					if(callable.getThreadPoolExecutor().getThreadPoolExecutor().getQueue().size()<=(callable.getQueueMaxsize()-callable.getQueueReserveSize())) {
						break;
					}
					//future列表中,也不能存在太多元素,应该小于等待队列的长度
					if(callable.getFutureQueue().size()<=(callable.getQueueMaxsize())) {
						break;
					}
				}
			}
		}
		//使用线程池执行
		Future<PageQueryExcuteParam> future=callable.excuteBySonThread(excuteParam);
		//放入参数对象队列中,
		while(!callable.getFutureQueue().offer(future)) {
			callable.getFutureQueue().offer(future);
		}
	}

}
