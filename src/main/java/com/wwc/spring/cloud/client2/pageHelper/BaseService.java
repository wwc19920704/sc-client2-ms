package com.wwc.spring.cloud.client2.pageHelper;

import java.util.List;

public interface BaseService{
	
	 List<Long> queryExtremesIdByCons(PageDto pageDto);
	
	 List queryObjsByIds(List<Long> ids);
}
