package com.wwc.spring.cloud.client2.utils;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 	雪花算法生成全局唯一的id
 * 	第一个部分，是 1 个 bit：0，这个是无意义的。id毕竟是正整数，故此位取消
 *
 *	第二个部分是 41 个 bit：表示的是时间戳。 id毕竟是正整数，   故此位取消,放给时间戳使用,表示当前时间到一个固定时间的毫秒 2^41-1ms大概是69年,那么加上符号位给的那一位,大概是2^42-1ms==138年
 *
 *	第三个部分是 5 个 bit：表示的是机房 id，10001 
 *
 *	第四个部分是 5 个 bit：表示的是机器 id，1 1001。 机房 id和 机器 id,公司并没有那么多的机房,可以将这10用作业务相关的标识
 *
 *	第五个部分是 12 个 bit：表示的序号，就是某个机房某台机器上这一毫秒内同时生成的 id 的序号，0000 00000000。需要借助第三方组件去记录,比较麻烦,故采用6位随机数(0/1)
 *  
 *  min=00000 00000 00000 00000 00000 00000 00000 00000 00 00000  00000 000000 ===>0
 *	
 *	max=11111 11111 11111 11111 11111 11111 11111 11111 11 1111 11111 111111==>144115188075855871
 *
 *	@author wwc
 */
public class SnowGenerateService {
	
	protected static Logger logger=LoggerFactory.getLogger(SnowGenerateService.class);
	
	/**
	 * 公平锁
	 */
	private static ReentrantLock lock=new ReentrantLock(true);
	
	/**
	 * 用于存储每一毫秒数的
	 */
	private static ConcurrentHashMap<Long,Long> sequenceMap=new ConcurrentHashMap<>();
	
	/**
	 * 时间戳位数
	 */
	private  int timeStampBit=42;
	
	/**
	 * 机房id位数
	 */
	private  int  machineRoomIdBit=5;
	
	/**
	 * 计算机id位数
	 */
	private  int  machineIdBit=5;
	
    /**
     *  	生成随机数位数
     */
    private  int randomNumBit=6;
	
	/**
	 * 	机器编码 
	 */
	private  int machineFlag=1;
	
	/**
	 * 	机房编码 
	 */
	private  int machineRoomFlag=1;
	
    /**
     * 	起始时间戳  2020-01-01 00:00:00
     */
    private final static Long startTimes=1580486400062l;
    
    /**
     * 	构造方法校验
     */
	public SnowGenerateService() {
		/**
		 * 所有位数总和与long的最大 最大位数相比较
		 */
		if(totalBit()>toBinary(Long.MAX_VALUE).length()) {
			throw new RuntimeException("设置算法的位数:"+totalBit()+",超过64位");
		}
	}
	
	
	public SnowGenerateService(int timeStampBit, int machineRoomIdBit, int machineIdBit, int randomNumBit) {
		super();
		this.timeStampBit = timeStampBit;
		this.machineRoomIdBit = machineRoomIdBit;
		this.machineIdBit = machineIdBit;
		this.randomNumBit = randomNumBit;
		if(totalBit()>toBinary(Long.MAX_VALUE).length()) {
			throw new RuntimeException("设置算法的位数:"+totalBit()+",超过64位");
		}
	}



	/**
	 * 	获取总的位数,时间戳位数+机房id位数+机器id位数+随机数位数
	 * @return
	 */
	public int totalBit() {
		return this.timeStampBit+this.machineRoomIdBit+this.machineIdBit+this.randomNumBit;
	}
	
	/**
	 * 	 生成id唯一策略
	 * @param authReq
	 * @return  datestr+computerRoomFlag+computerFlag+random
	 * @throws Exception 
	 */
	public  Long gennerateId() throws Exception {
		//时间戳+机房id+机器id+随机数先各自有十进制转化为二进制,并根据各自的位数补零,最后在将二进制数转化为十进制
		return Long.parseLong(gennerateStr(),2);
	}
	
	/**
	 * 时间戳+机房id+机器id+随机数先各自有十进制转化为二进制,并根据各自的位数补零,最后在将二进制数转化为十进制
	 * @throws Exception 
	 */
	public String gennerateStr() throws Exception {
		StringBuffer sb=new StringBuffer(spplyZero(toBinary(getTimeStamp()), timeStampBit))
		.append(spplyZero(toBinary(machineRoomFlag), machineRoomIdBit))
		.append(spplyZero(toBinary(machineFlag), machineIdBit))
		.append(getRandomBinaryByLength(randomNumBit));
		//二进制位数
		if(sb.toString().length()!=totalBit()) {
			logger.info("str----sb"+sb.toString());
		}
		return sb.toString();
	}
	
	/**
	 *	 获取时间戳  当前时间减去开始的时间,目前是42位,2^42-1ms==138年
	 * @return
	 * @throws Exception 
	 */
	private Long getTimeStamp() throws Exception {
		//返回的时间戳
		Long datestr=null;
		try {
			//上锁
			lock.lock();
			//停顿10毫秒,保证每次进来获取的时间戳,都会有1毫秒的差
			Thread.currentThread().sleep(1);
			//获取的时间戳,是当前时间减去开始的时间
			datestr=new Date().getTime()-startTimes;
		} catch (Exception e) {
			logger.error(this.getClass().getSimpleName()+"getTimeStamp,获取时间戳异常",e.getMessage(),e);
			throw e;
		}finally {
			lock.unlock();
		}
		return datestr;
	}
	/**
	 * 	获取长度为length的随机数(0-9)
	 * @param length
	 * @return
	 */
	public  String getRandomByLength(int length) {
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < length; i++) {
			//取0-9之间的整数
			int ch=(int) (Math.random()*10);
			sb.append(ch);
		}
		return sb.toString().trim();
	}
	
	/**
	     *     随机获取0/1,数字>=4生成0,数字<4生成1从概率上来说是相等的
	 * @param length  位数   
	 * @return
	 */
	public  String getRandomBinaryByLength(int length) {
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < length; i++) {
			//取0-9之间的整数
			int ch=(int) (Math.random()*10);
			//数字>=4生成0
			if(ch>=4) {
				sb.append(0);
			}
			//数字<4生成1
			if(ch<4) {
				sb.append(1);
			}
		}
		return sb.toString().trim();
	}
	
	/**
	 * 	在数字前面补0
	 * @param no  具体的字符串
	 * @param bit 一共多少位
	 * @return
	 */
	public String spplyZero(String no,int bit) {
		int supplybit=0;
		if(no.length()<bit) {
			supplybit=bit-no.length();
		}
		for (int i = 0; i < supplybit; i++) {
			no=0+no;
		}
		return no;
	}
	
	/**
	 * 	十进制转化为二进制
	 * @param num
	 * @return
	 */
	public String toBinary(long num) {
		//余数
		Long remainderNum =0l;
		//被除数
		Long divisorNum=2l;
		//结果
		StringBuffer result=new StringBuffer();
		for (int i = 0; ; i++) {
			if(num<=0) {
				break;
			}
			if(num%divisorNum==0) {
				remainderNum=0l;
			}else {
				remainderNum=1l;
			}
			result.append(remainderNum);
			num=num/divisorNum;
		}
		//倒序输出
		return result.reverse().toString();
	}
	public int getTimeStampBit() {
		return timeStampBit;
	}
	public void setTimeStampBit(int timeStampBit) {
		this.timeStampBit = timeStampBit;
	}
	public int getMachineRoomIdBit() {
		return machineRoomIdBit;
	}
	public void setMachineRoomIdBit(int machineRoomIdBit) {
		this.machineRoomIdBit = machineRoomIdBit;
	}
	public int getMachineIdBit() {
		return machineIdBit;
	}
	public void setMachineIdBit(int machineIdBit) {
		this.machineIdBit = machineIdBit;
	}
	public int getRandomNumBit() {
		return randomNumBit;
	}
	public void setRandomNumBit(int randomNumBit) {
		this.randomNumBit = randomNumBit;
	}

	public int getMachineFlag() {
		return machineFlag;
	}

	public void setMachineFlag(int machineFlag) {
		this.machineFlag = machineFlag;
	}

	public int getMachineRoomFlag() {
		return machineRoomFlag;
	}

	public void setMachineRoomFlag(int machineRoomFlag) {
		this.machineRoomFlag = machineRoomFlag;
	}
	
}
