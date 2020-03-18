package com.wwc.spring.cloud.client2.pageHelper.callBack;

import org.springframework.stereotype.Component;

import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;

@Component
public class SelectContractCallback extends PageCallBack{

	@Override
	public void excuteByMainThreadAfter(PageQueryExcuteParam sonThreadParam) throws Exception {
		// TODO Auto-generated method stub
		logger.info("----------------singel-------------size==="+sonThreadParam.getIdList().size());
	}

	@Override
	public PageQueryExcuteParam excute(PageQueryExcuteParam sonThreadParam) throws Exception {
		// TODO Auto-generated method stub
		Thread.currentThread().sleep(2000);
		for (Long contractIndex : sonThreadParam.getIdList()) {
			logger.info("--------------singel---------------id==="+contractIndex);
		}
		return sonThreadParam;
	}
}
