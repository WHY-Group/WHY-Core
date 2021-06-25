package com.why.baseframework.base.aop;

import java.lang.annotation.*;

/**
 * @author W
 * @description:
 * @title: RequestSource
 * @projectName tianrui
 * @date 2021年05月30日
 * @company WHY-Group
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceRequest {
    String[] value() default "";
}
