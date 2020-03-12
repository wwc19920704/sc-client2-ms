package com.wwc.spring.cloud.client2;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {
	
	protected static Logger logger=LoggerFactory.getLogger(BaseTest.class);
	
}
