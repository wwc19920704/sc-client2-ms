package com.wwc.spring.cloud.client2.config;

import java.util.Locale;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@MapperScan(basePackages={"com.wwc.spring.cloud.client2.dao"})
@ComponentScan(basePackages= {"com.wwc.spring.cloud"})
@EnableAsync
public class ApplicationConfig {

	/**
	 * 配置国际化为中文
	 * @return
	 */
    @Bean
    public LocaleResolver localeResolver() {
        FixedLocaleResolver sessionLocaleResolver = new FixedLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.CHINA);
        return sessionLocaleResolver;
    }
    

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource()
    {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager()
    {
        return new DataSourceTransactionManager(dataSource());
    }

}
