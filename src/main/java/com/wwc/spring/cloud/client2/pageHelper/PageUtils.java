package com.wwc.spring.cloud.client2.pageHelper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageDto;
import com.wwc.spring.cloud.client2.pageHelper.dto.PageQueryExcuteParam;
import com.wwc.spring.cloud.client2.pageHelper.excute.Excute;
import com.wwc.spring.cloud.client2.pageHelper.excute.MutilThreadExcute;
import com.wwc.spring.cloud.client2.pageHelper.excute.SigngleThreadExcute;
import com.wwc.spring.cloud.client2.pageHelper.service.BaseService;

/**
 * 	分页组件
 * @author wwc
 *
 */
public class PageUtils {
	
	private static Logger logger = LoggerFactory.getLogger(PageUtils.class);
	
	/**
	 * 单线程执行回调
	 */
	private static Excute single=new SigngleThreadExcute();
	
	/**
	 * 	多线程执行回调
	 */
	private static Excute mutil=new MutilThreadExcute();
	
	/**
	 * 	分页查询方法,使用场景:只能在前端页面不展示的情况下，且需要对每一次的结果集做出处理的时使用
	 * @param baseDao 
	 * @param pageDto 分页参数
	 * @param callBack 回调函数
	 * @param size 每页数量
	 */
	public static void queryExcute(BaseService baseService,
			PageDto pageDto,PageCallBack callBack,Object ret)throws Exception {
		pageQuery(baseService, pageDto, callBack, ret, single);
	}
	
	/**
	 * 	依赖jdk的future执行多线程任务
	 * @param baseService
	 * @param pageDto
	 * @param callable
	 * @param ret
	 * @throws Exception
	 */
	public static void queryExcuteByMutilThreads(BaseService baseService,
			PageDto pageDto,MutilThreadPageCallback callable,Object ret)throws Exception {
		//查询
		pageQuery(baseService, pageDto, callable, ret, mutil);
		//消费参数队列中的数据
		consume(callable);
		//阻塞,等待多线程任务全部完成
		blockForWait(callable.getFutureQueue());
	}
	
	/**
	 * 	阻塞,等待所有的任务结束
	 * @param queue
	 */
	private static void blockForWait(ConcurrentLinkedQueue<Future<PageQueryExcuteParam>> queue) {
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
	 * 将参数队列中的所有参数全部消费
	 * @param callable
	 * @param threadPoolExecutor
	 */
	private static void consume(MutilThreadPageCallback callable) {
		// TODO Auto-generated method stub
		logger.debug("--------------wait consume  finish-------------------");
		//参数对象
		PageQueryExcuteParam param=null;
		//取出参数对象
		while((param=callable.getParamQueue().peek())!=null) {
			//判断参数对象的isProcess属性是否是未处理,若isProcess状态为处理则不再处理
			if(param.getIsProcess()==PageQueryExcuteParam.NOT_PROCESS) {
				//将isProcess属性设置为处理中
				param.setIsProcess(PageQueryExcuteParam.PROCESSING);
				//放入线程池,异步处理
				callable.getFutureQueue().offer(callable.getThreadPoolExecutor().submit(callable));
			}
			if(param.getIsProcess()==PageQueryExcuteParam.PROCESSED) {
				callable.getParamQueue().poll();
			}
		}
		logger.debug("--------------consume have finished-------------------");
	}
	
	/**
	 * 分页查询
	 * @throws Exception 
	 */
	private static void pageQuery(BaseService baseService,
			PageDto pageDto,PageCallBack callBack,Object ret,Excute excute) throws Exception {
		logger.debug("start_page,page_cons=="+JSON.toJSONString(pageDto)+",baseDao="+baseService);
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
		//所有符合条件的主键的数量
		int  count=0;
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
				//主键数量+1
				count=count+idList.size();
				//正序排序;升序排序
				Collections.sort(idList);
				//获取最大的元素
				lastIndexInList=idList.get(idList.size() - 1);
				//头index设为本次循环的最大id+1,下次循环从id+1开始[lastIndexInList+1,tailIndex)
				lastIndexInList++;
				//对象结果集
				List objs=null;
				//创建一个子线程的参数对象
				PageQueryExcuteParam sonThreadParam=new PageQueryExcuteParam();
				//设置主键集合
				sonThreadParam.setIdList(idList);
				if(null!=ret) {
					//设置返回值
					sonThreadParam.setRet(ret);	
				}
				//判断是否需要查询实际的对象信息
				if(pageDto.isNeedObjList()) {
					//查询对象信息
					objs=baseService.queryObjsByIds(idList);
					sonThreadParam.setObjs(objs);
				}
				//执行主逻辑,单线程/多线程
				excute.excute(callBack,sonThreadParam);
				//后置处理,同步
				callBack.excuteByMainThreadAfter(sonThreadParam);
			}
			//主键最大的作为下次查询的起点
			headIndex=lastIndexInList;
			queryTimes++;
		}
		logger.debug("end_page,queryTimes=="+queryTimes+",count by queried=="+count);
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

