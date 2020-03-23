package com.wwc.spring.cloud.client2.pageHelper.excute;

import com.wwc.spring.cloud.client2.pageHelper.MutilThreadPageCallback;
import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;

/**
 * 	多线程处理执行器
 * @author wwc
 *
 */
public class MutilThreadExcute extends Excute{

	@Override
	public void excute(PageCallBack pageCallBack,PageQueryExcuteParam excuteParam) throws Exception{
		// TODO Auto-generated method stub
		MutilThreadPageCallback callable=(MutilThreadPageCallback) pageCallBack;
		//放入参数对象队列中,
		while(!callable.getParamQueue().offer(excuteParam)) {
			callable.getParamQueue().offer(excuteParam);
		}
	}
}
