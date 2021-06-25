package com.why.baseframework.redis;

/**
 * @Author Y
 * @Description: redis中各个过期时间配置
 * @Title: RedisTimeOutConstants
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
public final class RedisTimeOutConstants {

    /**
     * 登录过期秒数
     */
    public static final Integer REDIS_LOGIN_USER_TIMEOUT = 60 * 30;

    /**
     * 登录过期秒数节点
     */
    static final Integer REDIS_LOGIN_USER_EXPIRE = 60 * 25;

}
