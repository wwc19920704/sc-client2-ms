package com.wwc.spring.cloud.client2.pageHelper.excute;

import com.wwc.spring.cloud.client2.pageHelper.PageCallBack;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;

/**
 * 	查询到结果集之后的处理抽象类
 * @author wwc
 *
 */
public abstract class Excute {
	
	/**
	 * 	查询之后的执行方法
	 * @param pageCallBack
	 * @param excuteParam
	 */
	public abstract void excute(PageCallBack pageCallBack,PageQueryExcuteParam excuteParam)throws Exception;
}
