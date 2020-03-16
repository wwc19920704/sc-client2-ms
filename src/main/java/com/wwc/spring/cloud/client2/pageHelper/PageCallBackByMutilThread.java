package com.wwc.spring.cloud.client2.pageHelper;

import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多线程方式执行
 * @author USER
 */
public abstract class PageCallBackByMutilThread{
	
	protected static Logger logger=LoggerFactory.getLogger(PageCallBackByMutilThread.class);

	/**
	 * 分页查询得到结果集之后的操作,异步操作
	 * @param result 实体结果集
	 * @param ids	主键集合
	 */
	public abstract Future excuteBySonThread(List result,List<Long> ids) throws Exception;
	
	/**
	 * 在主线程中的后置处理接口,同步操作
	 * @param result
	 * @param ids
	 * @param ret 返回值  对主线程中的实参的处理,建议主线程处理
	 * @return
	 * @throws Exception
	 */
	public abstract void excuteByMainThreadAfter(List result,List<Long> ids,Object ret) throws Exception;
	
}
