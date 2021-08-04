package com.why.baseframework.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author W
 */
@Configuration
public class TransactionManagerConfig {
    /**
     * mongodb用的事务管理器
     * @param dbFactory the dbFactory
     * @return MongoTransactionManager mongodb事务管理器
     * @author chenglin.wu
     * @date: 2021/8/4
     */
    @Bean("mongoTransactionManager")
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
    /**
     * mysql事务管理器
     * @param dataSource the dataSource
     * @return DataSourceTransactionManager
     * @author chenglin.wu
     * @date: 2021/8/4
     */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
