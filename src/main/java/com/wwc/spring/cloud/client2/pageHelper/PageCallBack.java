package com.wwc.spring.cloud.client2.pageHelper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PageCallBack {
	
	protected static Logger logger=LoggerFactory.getLogger(PageCallBack.class);

	/**
	 * 分页查询得到结果集之后的操作
	 * @param result 实体结果集
	 * @param ids	主键集合
	 * @param 返回值
	 */
	public abstract void excute(List result,List<Long> ids,Object ret) throws Exception;
}
