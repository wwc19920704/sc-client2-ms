package com.wwc.spring.cloud.client2.pageHelper.excute;

import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;

/**
 * 	单线程处理执行器
 * @author wwc
 *
 */
public class SigngleThreadExcute extends Excute{

	@Override
	public void excute(PageCallBack pageCallBack,PageQueryExcuteParam excuteParam) throws Exception {
		// TODO Auto-generated method stub
		pageCallBack.excute(excuteParam);
	}

}
