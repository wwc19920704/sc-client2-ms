package com.wwc.spring.cloud.client2.pageHelper.dto;

/**
 * 	根据主键分页公共dto
 * @author wwc
 *
 */
public class PageDto {
	
	/**
	 * 	降序排序
	 */
	public final static String ORDER_DESC="DESC";
	
	/**
	 * 	升序排序
	 */
	public final static String ORDER_ASC="ASC";
	
	/**
	 * 	默认使用的分页每页的数量
	 */
	public  static Integer LIMIT_SIZE=10;
	
	public static int FIVTY_THOUSAND=50000;
	
	public static int FIVE_THOUSAND=5000;
	
	public static int FIV_HUNDRED=500;
	
	public static int ONE_HUNDRED=100;
	
	public static int HALF_HUNDRED=50;
	
	public  static int ONE=1;
	
	/**
	 * 	分页时的开始主键id
	 */
	private Long headIndex;
	
	/**
	 * 	分页时的末尾主键id(不包含)
	 */
	private Long tailIndex;
	
	/**
	 *	 排序顺序
	 */
	private String order;
	
	/**
	 * 	每次取的条数
	 */
	private Integer pageSize=LIMIT_SIZE;
	
	/**
	 * 是否需要返回查询到的对象结果集
	 */
	private boolean isNeedObjList=false;
	
	/**
	 * 查询次数
	 * 
	 */
	private Integer queryTimes;
	
	/**
	 * 用户自定义每次查询的数量
	 */
	private Integer queryLimitCustomize=FIV_HUNDRED;

	public Integer getQueryTimes() {
		return queryTimes;
	}

	public void setQueryTimes(Integer queryTimes) {
		this.queryTimes = queryTimes;
	}

	public Integer getQueryLimitCustomize() {
		return queryLimitCustomize;
	}

	public void setQueryLimitCustomize(Integer queryLimitCustomize) {
		this.queryLimitCustomize = queryLimitCustomize;
	}

	public boolean isNeedObjList() {
		return isNeedObjList;
	}

	public void setNeedObjList(boolean isNeedObjList) {
		this.isNeedObjList = isNeedObjList;
	}

	public Long getHeadIndex() {
		return headIndex;
	}

	public void setHeadIndex(Long headIndex) {
		this.headIndex = headIndex;
	}

	public Long getTailIndex() {
		return tailIndex;
	}

	public void setTailIndex(Long tailIndex) {
		this.tailIndex = tailIndex;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
