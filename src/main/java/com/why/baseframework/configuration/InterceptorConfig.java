package com.why.baseframework.configuration;

import com.why.baseframework.base.web.interceptor.CorsInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: CorsInterceptor
 * @ProjectName base_framework
 * @Date 2021/4/16
 * @Company  WHY-Group
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Value("${access.control.allow.origin:*}")
    private String accessControlAllowOrigin;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsInterceptor(accessControlAllowOrigin)).addPathPatterns("/**");
    }
}
