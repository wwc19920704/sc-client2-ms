package com.wwc.spring.cloud.client2.pageHelper.service;

import java.util.List;

import com.wwc.spring.cloud.client2.pageHelper.dto.PageDto;

/**
 * 	基础的service，必须要实现queryExtremesIdByCons. 
 * queryObjsById接口可以不实现,根据个人需求,不需要查询对应的实体信息时可以不实现
 * @author wwc
 *
 */
public interface BaseService{
	
	/**
	 * 	查询符合条件的最大/最小的主键,或者是在原有的条件的基础上的增加主键区间[headIndex,tailIndex)内的id集合
	 * 	用法:
	 * 	1。查询条件需要继承com.wwc.spring.cloud.client2.pageHelper.PageDto,并扩展自己的查询条件
	 * 	2.queryExtremesIdByCons方法的实现里需要将父类方法的入参强转为自己扩展的子类型的实例,然后调用传入dao方法
	 * 	3.dao方法对应的sql写法,查询参数就是扩展出来的com.wwc.spring.cloud.client2.pageHelper.PageDto的子类
	 * 		select   pk
	 *      from  table 
	 *      where valid=0 
	 *      ........(其他条件)
	 *      <!-- [headIndex,tailIndex) 左闭右开区间-->
	 *  	<if test="queryDto.headIndex!=null and queryDto.tailIndex!=null">
	 *   		and pk &gt;=#{queryDto.headIndex}  and  pk &lt;#{queryDto.tailIndex}
	 *  	</if>
	 *  	<if test="queryDto.order !=null">
	 *   		<!-- 此处 queryDto.order的取值只有desc,aesc-->
	 *   		order by pk ${queryDto.order}
	 *  	</if>
	 *  	<if test="queryDto.pageSize !=null">
	 *   		limit #{queryDto.pageSize}
	 *  	</if> 
	 * @param pageDto
	 * @return
	 */
	 List<Long> queryExtremesIdByCons(PageDto pageDto);
	
	 /**
	  *          	根据主键的id集合查询所有的实例具体信息
	  * @param ids
	  * @return
	  */
	 List queryObjsByIds(List<Long> ids);
}
