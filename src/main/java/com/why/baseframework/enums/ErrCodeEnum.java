package com.why.baseframework.enums;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: ReflectionUtils
 * @Date 2021/4/22
 * @Company  WHY-Group
 */
public enum ErrCodeEnum {
    /**
     * 请求处理成功
     */
    SUCCESS(0),
    /**
     * 410 未知
     */
    UNKNOWN(410),
    /**
     * AUTH: 401未授权
     */
    AUTH(401),
    /**
     * 登录过期
     */
    LOGIN_EXPIRED(402),
    /**
     * 强制下线
     */
    FORCED_OFFLINE(403),
    /**
     * DATACHECK: 412未满足数据校验
     */
    DATA_CHECK(412),
    /**
     * 业务异常
     */
    BUSINESS(9990),
    /**
     * THROWABLE: 9999程序异常
     */
    THROWABLE(9999),
    /**
     * 请求来源异常
     */
    REQUEST_SOURCE(420),
    /**
     * 访问受限
     */
    LIMITED_ACCESS(430);

    /**
     * 错误代码
     */
    private int code;

    /**
     * @param code 错误码
     * @return null
     * @author chenglin.wu
     * @date: 2021/4/22
     */
    ErrCodeEnum(int code) {
        this.code = code;
    }

    /**
     * 获取错误码
     *
     * @return int
     * @author chenglin.wu
     * @date: 2021/4/22
     */
    public int getCode() {
        return code;
    }

}
