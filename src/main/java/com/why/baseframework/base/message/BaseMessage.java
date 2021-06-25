package com.why.baseframework.base.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;


/**
 * @Author Y
 * @Description: i18n, 基础Message，需要在resources创建对应properties
 * @Title: BaseMessage
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
@Component
public class BaseMessage {

    @Autowired
    private MessageSource messageSource;

    /**
     * 返回对应Locale的语言的返回值
     * @param returnStr
     * @Author Y
     * @Description
     * @Date 2021/4/15 22:32
     **/
    public String getMessageString(String returnStr) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(returnStr, null, locale);
        } catch (Exception e) {
            return returnStr;
        }
    }

}
