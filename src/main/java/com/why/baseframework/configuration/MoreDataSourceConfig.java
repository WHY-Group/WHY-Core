package com.why.baseframework.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author bin.hu
 * @description: 多数据源配置类
 * @title: MoreDataSourceConfig
 * @projectName baseframework
 * @date 2021年4月21日
 * @company  WHY-Group
 */
public class MoreDataSourceConfig extends AbstractRoutingDataSource {

	private static final ThreadLocal<DataSourceType> dbName = new ThreadLocal<>();

	public static void setDbName(DataSourceType type) {
		dbName.set(type);
	}

	public static DataSourceType getDbName() {
		return dbName.get();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		DataSourceType key = getDbName();
		if (key != null) {
			return key;
		}
		return DataSourceType.DEFAULT;
	}

}
