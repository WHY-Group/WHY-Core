package com.why.baseframework.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author bin.hu
 * @description: 配置多数据源
 * @title: DataSourceConfig
 * @projectName baseframework
 * @date 2021年4月21日
 * @company  WHY-Group
 */
@Configuration
public class DataSourceConfig {

	@Autowired
	private Environment env;

	public DataSource dataSource1() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
//		dataSource.setInitialSize(Integer.valueOf(env.getProperty("spring.datasource.initial-size")));
//		dataSource.setMaxActive(Integer.valueOf(env.getProperty("spring.datasource.max-active")));
//		dataSource.setMinIdle(Integer.valueOf(env.getProperty("spring.datasource.min-idle")));
//		dataSource.setMaxWait(Integer.valueOf(env.getProperty("spring.datasource.max-wait")));
		return dataSource;
	}

	public DataSource dataSource2() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName2"));
		dataSource.setUrl(env.getProperty("spring.datasource.url2"));
		dataSource.setUsername(env.getProperty("spring.datasource.username2"));
		dataSource.setPassword(env.getProperty("spring.datasource.password2"));
//		dataSource.setInitialSize(Integer.valueOf(env.getProperty("spring.datasource.initial-size")));
//		dataSource.setMaxActive(Integer.valueOf(env.getProperty("spring.datasource.max-active")));
//		dataSource.setMinIdle(Integer.valueOf(env.getProperty("spring.datasource.min-idle")));
//		dataSource.setMaxWait(Integer.valueOf(env.getProperty("spring.datasource.max-wait")));
		return dataSource;
	}

	@Bean("dataSource")
	public DataSource getDataSource() {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceType.DEFAULT, dataSource1());
		targetDataSources.put(DataSourceType.SOURCE2, dataSource2());
		MoreDataSourceConfig dataSources = new MoreDataSourceConfig();
		dataSources.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
		dataSources.setDefaultTargetDataSource(targetDataSources.get(DataSourceType.DEFAULT));// 默认的datasource
		return dataSources;
	}
}
