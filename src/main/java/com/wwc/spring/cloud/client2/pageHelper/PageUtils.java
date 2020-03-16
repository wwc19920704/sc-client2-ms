package com.wwc.spring.cloud.client2.pageHelper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 	分页组件
 *  组件依赖spring框架
 * @author wwc
 *
 */
public class PageUtils {
	
	protected static Logger logger = LoggerFactory.getLogger(PageUtils.class);
	
	/**
	 * 	分页查询方法,使用场景:只能在前端页面不展示的情况下，且需要对每一次的结果集做出处理的时使用
	 * @param baseDao 
	 * @param pageDto 分页参数
	 * @param callBack 回调函数
	 * @param size 每页数量
	 */
	public static void queryExcute(BaseService baseService,
			PageDto pageDto,PageCallBack callBack,Object result)throws Exception {
		logger.debug("start_page,page_cons=="+JSON.toJSONString(pageDto)+",baseDao="+baseService);
		//初始化最大id
		Long maxId=0l;
		//初始化最小id
		Long minId=0l;
		//查询最大值时，每次只能取一个
		pageDto.setPageSize(PageDto.ONE);
		//找最大的倒序排除
		pageDto.setOrder(PageDto.ORDER_DESC);
		List<Long> maxIdReult=baseService.queryExtremesIdByCons(pageDto);
		if(maxIdReult!=null&&!maxIdReult.isEmpty()) {
			maxId=maxIdReult.get(0);
		}
		logger.debug("page_maxId ="+maxId);
		//找最小的主键,正序排除
		pageDto.setOrder(PageDto.ORDER_ASC);
		List<Long> minIdresults=baseService.queryExtremesIdByCons(pageDto);
		if(minIdresults!=null&& !minIdresults.isEmpty()) {
			minId=minIdresults.get(0);
		}
		logger.debug("page_minId="+minId);
		//循环查..
		//头指针
		Long headIndex=minId;
		//尾指针
		Long tailIndex=++maxId;
		//循环次数
		int queryTimes=1;
		//本次查询的最大index
		Long lastIndexInList=0l;
		while(circuitCondition(pageDto, queryTimes, headIndex, tailIndex)) {
			//组装查询条件
			pageDto.setHeadIndex(headIndex);
			pageDto.setTailIndex(tailIndex);
			pageDto.setOrder(null);
			pageDto.setPageSize(pageDto.getQueryLimitCustomize());
			///查询出符合条件的id集合
			List<Long> idList=baseService.queryExtremesIdByCons(pageDto);
			if(idList!=null&& !idList.isEmpty()) {
				//正序排序;升序排序
				Collections.sort(idList);
				//获取最大的元素
				lastIndexInList=idList.get(idList.size() - 1);
				//头index设为本次循环的最大id+1,下次循环从id+1开始[lastIndexInList+1,tailIndex)
				lastIndexInList++;
				//对象结果集
				List objs=null;
				if(pageDto.isNeedObjList()) {
					objs=baseService.queryObjsByIds(idList);
					if(objs!=null && !objs.isEmpty()) {
						//执行回调函数的方法
						callBack.excute(objs,idList,result);
					}
				}else {
					//执行混调函数里的方法
					callBack.excute(null,idList,result);
				}
			}
			//起始的index增加制定的主键偏移量
			headIndex+=pageDto.getPrimaryDifference();
			//比较,谁大用谁,以减少不必要的查询
			if(headIndex<lastIndexInList) {
				headIndex=lastIndexInList;
			}
			queryTimes++;
		}
		logger.debug("end_page,queryTimes=="+queryTimes);
	}
	
	
	/**
	 * 分页查询多线程处理
	 * @param baseService 实现BaseService的实现类
	 * @param pageDto 分页参数
	 * @param callBack 回调函数
	 * @param result  主线程中要随着子线程的执行而做出处理的变量
	 * @throws Exception
	 */
	public static void queryExcuteByMutilThreads(BaseService baseService,
			PageDto pageDto,PageCallBackByMutilThread callable,Object result)throws Exception {
		logger.debug("start_page,page_cons=="+JSON.toJSONString(pageDto)+",baseDao="+baseService);
		//存储future对象的同步队列
		ConcurrentLinkedQueue<Future> queue=new ConcurrentLinkedQueue<>();
		//初始化最大id
		Long maxId=0l;
		//初始化最小id
		Long minId=0l;
		//查询最大值时，每次只能取一个
		pageDto.setPageSize(PageDto.ONE);
		//找最大的倒序
		pageDto.setOrder(PageDto.ORDER_DESC);
		//幂等处理最大index,有则使用,无则获取
		if(null==pageDto.getTailIndex()) {
			List<Long> maxIdReult=baseService.queryExtremesIdByCons(pageDto);
			if(maxIdReult!=null&&!maxIdReult.isEmpty()) {
				maxId=maxIdReult.get(0);
			}
		}else {
			maxId=pageDto.getTailIndex();
		}
		logger.debug("page_maxId ="+maxId);
		//找最小的主键,正序
		pageDto.setOrder(PageDto.ORDER_ASC);
		//幂等处理最小index,有则使用,无则获取
		if(null==pageDto.getHeadIndex()) {
			List<Long> minIdresults=baseService.queryExtremesIdByCons(pageDto);
			if(minIdresults!=null&& !minIdresults.isEmpty()) {
				minId=minIdresults.get(0);
			}
		}else {
			minId=pageDto.getHeadIndex();
		}
		logger.debug("page_minId="+minId);
		//循环查..
		//头指针
		Long headIndex=minId;
		//尾指针
		Long tailIndex=++maxId;
		//循环次数
		int queryTimes=1;
		//本次查询的最大index
		Long lastIndexInList=0l;
		//开始循环
		while(circuitCondition(pageDto, queryTimes, headIndex, tailIndex)) {
			//组装查询条件
			pageDto.setHeadIndex(headIndex);
			pageDto.setTailIndex(tailIndex);
			pageDto.setOrder(null);
			pageDto.setPageSize(pageDto.getQueryLimitCustomize());
			//查询出符合条件的id集合
			List<Long> idList=baseService.queryExtremesIdByCons(pageDto);
			if(idList!=null&& !idList.isEmpty()) {
				//正序排序;升序排序
				Collections.sort(idList);
				//获取最大的元素
				lastIndexInList=idList.get(idList.size() - 1);
				//头index设为本次循环的最大id+1,下次循环从id+1开始[lastIndexInList+1,tailIndex)
				lastIndexInList++;
				//对象结果集
				List objs=null;
				//返回的future对象
				Future future=null;
				//判断是否需要查询实际的对象信息
				if(pageDto.isNeedObjList()) {
					//查询对象信息
					objs=baseService.queryObjsByIds(idList);
					//判断集合是否为空
					if(objs!=null && !objs.isEmpty()) {
						//子线程异步处理
						future=callable.excuteBySonThread(objs,idList);
						//主线程后置操作
						callable.excuteByMainThreadAfter(objs,idList,result);
					}
				}else {
					//子线程异步处理
					future=callable.excuteBySonThread(null,idList);
					//主线程后置操做
					callable.excuteByMainThreadAfter(null,idList,result);
				}
				//放入同步队列中,
				while(!queue.offer(future)) {
					queue.offer(future);
				}
			}
			//起始的index增加制定的主键偏移量
			headIndex+=pageDto.getPrimaryDifference();
			//比较,谁大用谁,以减少不必要的查询
			if(headIndex<lastIndexInList) {
				headIndex=lastIndexInList;
			}
			queryTimes++;
		}
		logger.debug("end_page,queryTimes=="+queryTimes);
		//阻塞,等待多线程任务全部完成
		blockForWait(queue);
	}
	
	/**
	 * 阻塞主线程,等待子线程结束
	 * @param queue
	 * @return
	 */
	static void blockForWait(ConcurrentLinkedQueue<Future> queue) {
		logger.debug("--------------wait sonThread excute finish-------------------");
		Future result=null;
		while((result=queue.peek())!=null) {
			if(result.isDone()) {
				logger.debug("--------------thread process ok?-------------------=="+result.isDone());
				queue.poll();
			}
		}
		logger.debug("--------------all sonThread have excuted finished-------------------");
	}
	
	/**
	 * 判断循环条件是否成立
	 * @param pageDto 分页参数
	 * @param queryTimes 当前查询次数
	 * @param headIndex
	 * @param headIndex
	 * @return
	 */
	private static boolean circuitCondition(PageDto pageDto,int queryTimes,Long headIndex,Long tailIndex) {
		//查询次数限制不为空或者查询次数大于0,则判断条件,优先使用此
		if(null!=pageDto.getQueryTimes()
		  && 0<pageDto.getQueryTimes()) {
			if(pageDto.getQueryTimes()<queryTimes) {
				return false;
			}
			if(headIndex>tailIndex) {
				return false;
			}
			return true;
		}
		return (headIndex<=tailIndex)?true:false; 
	}
}
