package com.wwc.spring.cloud.client2.pageHelper.excute;

import com.wwc.spring.cloud.client2.pageHelper.MutilPageCallback;
import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;

public class MutilThreadExcute extends Excute{

	@Override
	public void excute(PageCallBack pageCallBack,PageQueryExcuteParam excuteParam) throws Exception{
		// TODO Auto-generated method stub
		MutilPageCallback callable=(MutilPageCallback) pageCallBack;
		//放入参数对象队列中,
		while(!callable.getParamQueue().offer(excuteParam)) {
			callable.getParamQueue().offer(excuteParam);
		}
	}
}
