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
		//使用线程池执行
		Future<PageQueryExcuteParam> future=callable.excuteBySonThread(excuteParam);
		//放入参数对象队列中,
		while(!callable.getFutureQueue().offer(future)) {
			callable.getFutureQueue().offer(future);
		}
	}

}
