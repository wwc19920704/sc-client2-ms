package com.wwc.spring.cloud.client2.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wwc.spring.cloud.client2.dto.SelectContractDto;

public interface ProductLoanContractDao {
    
	public List<Long> selectPage(@Param("queryDto")SelectContractDto selectContractDto);
}