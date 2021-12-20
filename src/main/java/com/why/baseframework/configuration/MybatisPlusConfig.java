package com.why.baseframework.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.why.baseframework.constants.IntConstants;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author W
 * @description:
 * @title: MybatisPlusConfig
 * @projectName WHY-Core
 * @date 2021年05月16日
 * @company WHY-Group
 */
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {
    /**
     * mybatis-plus 分页插件的注入
     *
     * @return MybatisPlusInterceptor
     * @author W
     * @date 2021/5/16
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", new Date(), metaObject);
        setFieldValByName("updateTime", new Date(), metaObject);
        setFieldValByName("delFlag",String.valueOf(IntConstants.INT_0),metaObject);
        setFieldValByName("isUse",String.valueOf(IntConstants.INT_1),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", new Date(), metaObject);
    }
}
