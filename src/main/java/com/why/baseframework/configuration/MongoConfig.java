package com.why.baseframework.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author W
 */
@Slf4j
@Configuration
public class MongoConfig {
    /**
     * 解决将数据存入mongodb的时候出现的 _class 的字段
     * @param factory the mongoDatabaseFactory
     * @param context the mongoContext
     * @param beanFactory the beanFactory
     * @return MappingMongoConverter
     * @author W
     * @date: 2021/8/4
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(final MongoDatabaseFactory factory, final MongoMappingContext context, final BeanFactory beanFactory) {
        final DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        final MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        try {
            mappingConverter.setCustomConversions(beanFactory.getBean(MongoCustomConversions.class));
        }
        catch (NoSuchBeanDefinitionException ex) {
            log.error("mappingMongoConverter Exception: ",ex);
        }
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingConverter;
    }
}
