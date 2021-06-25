package com.why.baseframework.constants;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: DateConstants
 * @ProjectName base_framework
 * @Date 2021/4/19
 * @Company  WHY-Group
 */
public final class DateConstants {
    /**
     * 私有化常量类的构造器
     */
    private DateConstants() {
    }

    /**
     * 默认日期格式
     */
    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 默认日期时间格式
     */
    public final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * UTC 时间格式
     */
    public final static String DATE_TIME_UTC_FORMAT = "YYYYMMDD T HHMMSS Z";


    /**
     * 斜线类型的日期时间格式
     */
    public final static String DATE_TIME_OBLIQUE_FORMAT = "yyyy/MM/dd HH/mm/ss";

    /**
     * 斜线类型的日期格式
     */
    public final static String DATE_OBLIQUE_FORMAT = "yyyy/MM/dd";

    /**
     * UTC 的字符串
     */
    public final static String UTC_STR = " UTC";

}
