package com.wwc.spring.cloud.client2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wwc.spring.cloud.client2.dto.SelectContractDto;
import com.wwc.spring.cloud.client2.pageHelper.PageUtils;
import com.wwc.spring.cloud.client2.pageHelper.callBack.SelectContractPageCallable;
import com.wwc.spring.cloud.client2.pageHelper.callBack.SelectContractPageCallableFor;
import com.wwc.spring.cloud.client2.service.ProductLoanContractService;

@RestController
@RequestMapping("/index")
public class Controller {
	
	@Autowired
	private  ProductLoanContractService productLoanContractService;
	
	@Autowired
	private SelectContractPageCallable selectContractPageCallable;
	
	@Autowired
	private SelectContractPageCallableFor selectContractPageCallableFor;
	
	@RequestMapping("/excute")
	public Object excute() {
		try {
			PageUtils.queryExcuteByMutilThreads(productLoanContractService, getDto(), selectContractPageCallable, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ok";
	}
	
	@RequestMapping("/excute/For")
	public Object excuteFor() {
		try {
			PageUtils.queryExcuteByMutilThreads(productLoanContractService, getDto(), selectContractPageCallableFor, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ok";
	}
	
	private SelectContractDto getDto() {
		SelectContractDto selectContractDto=new SelectContractDto();
		selectContractDto.setNeedObjList(false);
		selectContractDto.setPrimaryDifference(selectContractDto.LIMIT_SIZE*selectContractDto.ONE_HUNDRED);
		selectContractDto.setQueryLimitCustomize(selectContractDto.LIMIT_SIZE);
//		selectContractDto.setQueryTimes(10);
		return selectContractDto;
	}
}
