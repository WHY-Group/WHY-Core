package com.why.baseframework.configuration;

import com.why.baseframework.base.web.interceptor.CustomerLocalChangeInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @Author Y
 * @Description: 国际化语言配置
 * @Title: LocaleConfig
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
@Configuration
public class LocaleConfig implements WebMvcConfigurer {
    private final MessageSource messageSource;

    public LocaleConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * @Return LocaleResolver
     * @Author Y
     * @Description 默认解析器 其中locale表示默认语言
     * @Date 2021/4/16 15:48
     **/
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.CHINA);
        return localeResolver;
    }

    /**
     * @Param registry
     * @Author Y
     * @Description 默认拦截器 其中lang表示切换语言的参数名
     * @Date 2021/4/16 15:48
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        CustomerLocalChangeInterceptor localeInterceptor = new CustomerLocalChangeInterceptor();
        registry.addInterceptor(localeInterceptor);
    }
    /**
     * Validation message i18n
     * @return Validator
     */
    @Bean
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(this.messageSource);
        return validator;
    }

}
