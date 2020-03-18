package com.wwc.spring.cloud.client2.pageHelper.dto;

import java.util.List;

/**
 * 	多线程执行的
 * @author wwc
 *
 */
public class PageQueryExcuteParam {
	
	public static final int PROCESSED=1;
	
	public static final int NOT_PROCESS=-1;
	
	public static final int PROCESSING=0;
	
	//对象结果集
	private List objs;
		
	private List<Long> idList;
		
	private Object ret;
		
	/**
	* -1  未处理  0 处理中  1已处理
	*/
	private int isProcess=-1;
	
	/**
	 * 	同步方法,只有两个线程争夺这把锁
	 * @return
	 */
	public synchronized int getIsProcess() {
		return isProcess;
	}

	/**
	 * 	同步方法,只有两个线程争夺这把锁
	 * @param isProcess
	 */
	public synchronized void setIsProcess(int isProcess) {
		this.isProcess = isProcess;
	}

	public Object getRet() {
		return ret;
	}

	public void setRet(Object ret) {
		this.ret = ret;
	}

	public List getObjs() {
		return objs;
	}

	public void setObjs(List objs) {
		this.objs = objs;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
}
