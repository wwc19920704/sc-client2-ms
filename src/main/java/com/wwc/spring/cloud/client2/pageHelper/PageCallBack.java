package com.wwc.spring.cloud.client2.pageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;



/**
 * 	分页查询回调函数
 * @author wwc
 *
 */
public abstract class PageCallBack {
	
	protected static Logger logger=LoggerFactory.getLogger(PageCallBack.class);

	/**
	 * 	在主线程中的后置处理接口,同步操作
	 * @param sonThreadParam
	 * @throws Exception
	 */
	public abstract void excuteByMainThreadAfter(PageQueryExcuteParam sonThreadParam) throws Exception;
	
	/**
	 * 	需要执行的主逻辑,
	 * @param sonThreadParam 参数
	 * @return
	 * @throws Exception
	 */
	public abstract PageQueryExcuteParam excute(PageQueryExcuteParam sonThreadParam) throws Exception;
}
