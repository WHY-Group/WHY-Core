package com.why.baseframework.constants;

/**
 * @Author Y
 * @Description: redis中登录用户token与userId后缀常量配置
 * @Title: RedisLoginUserSuffix
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
public final class RedisLoginUserSuffix {

    /**
     * 商城用户id后缀
     */
    public static final String MARKET_USER_ID="-market_user";

    /**
     * 卖家后台用户id后缀
     */
    public static final String SELLER_BACKSTAGE_USER = "-seller_backstage_user";

    /**
     * 运营平台用户id后缀
     */
    public static final String OPERATION_BACKSTAGE_USER="-operation_backstage_user";
}
