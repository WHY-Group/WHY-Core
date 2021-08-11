package com.why.baseframework.constants;

import org.springframework.data.domain.Sort;

/**
 * @Author Y
 * @Description: Mybatis-plus 中Wrapper 常用字段
 * @Title: WrapperConstants
 * @ProjectName oracle
 * @Date 2021/4/29
 * @Company WHY-Group
 */
public final class QueryConstants {

    /**
     * 数据库字段--创建
     */
    public static final String CREATE_TIME = "create_time";


    /**
     * 前端传送字段---开始时间
     */
    public static final String BEGIN_TIME = "beginTime";

    /**
     * 前端传送字段---结束时间
     */
    public static final String END_TIME = "endTime";

    /**
     * 默认排序order
     */
    public static final Sort.Order DEFAULT_ORDER = Sort.Order.desc(CREATE_TIME);

    /**
     * 默认的排序Sort
     */
    public static final Sort DEFAULT_SORT = Sort.by(DEFAULT_ORDER);

    /**
     * 倒序排列的字符串
     */
    public static final String DESC = "DESC";

    /**
     * 正序排列的字符串
     */
    public static final String ASC = "ASC";
}
