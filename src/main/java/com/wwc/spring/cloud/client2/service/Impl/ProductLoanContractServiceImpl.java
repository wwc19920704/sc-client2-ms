package com.wwc.spring.cloud.client2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wwc.spring.cloud.client2.dao.ProductLoanContractDao;
import com.wwc.spring.cloud.client2.dto.SelectContractDto;
import com.wwc.spring.cloud.client2.pageHelper.PageDto;
import com.wwc.spring.cloud.client2.service.ProductLoanContractService;

@Service
public class ProductLoanContractServiceImpl implements ProductLoanContractService{

	@Autowired
	private ProductLoanContractDao productLoanContractDao;
	
	@Override
	public List<Long> queryExtremesIdByCons(PageDto pageDto) {
		// TODO Auto-generated method stub
		SelectContractDto selectContractDto=(SelectContractDto) pageDto;
		List<Long> contractIndexList=productLoanContractDao.selectPage(selectContractDto);
		return contractIndexList;
	}

	@Override
	public List queryObjsByIds(List<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

}
