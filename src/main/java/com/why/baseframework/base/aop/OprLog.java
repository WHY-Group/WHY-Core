package com.why.baseframework.base.aop;

import com.why.baseframework.constants.OprType;

import java.lang.annotation.*;

/**
 * @author Y
 * @description
 * @title OprLog
 * @projectName tianrui
 * @date 2021/5/31 20:31
 * @company WHY-Group
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OprLog {

    String content() default "";

    String title() default "";

    OprType type() default OprType.OTHER;

}
