package com.wwc.spring.cloud.client2.pageHelper.callBack;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;

@Component
public class SelectContractCallback extends PageCallBack{

	@Override
	public void excute(List result, List<Long> ids, Object ret) throws Exception {
		Thread.currentThread().sleep(2000);
		for (Long contractIndex : ids) {
			logger.info("-----------------------------id==="+contractIndex);
		}
	}
}
