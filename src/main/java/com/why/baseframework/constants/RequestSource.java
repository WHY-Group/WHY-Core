package com.why.baseframework.constants;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: RequestSource
 * @ProjectName base_framework
 * @Date 2021/4/16 10:03
 * @Company  WHY-Group
 */
public final class RequestSource {
    /**
     * 私有化构造器
     */
    private RequestSource() {
    }

    /**
     * 请求来源关键字
     */
    public static final String REQUEST_SOURCE = "RequestSource";

    /**
     * token关键字
     */
    public static final String TOKEN = "Authorization";

    /**
     * token 前缀
     */
    public static final String TOKEN_SUFFIX = "Bearer ";

    /**
     * 请求来源为商城
     */
    public static final String BACK_STAGE = "backStage";

    /**
     * 请求来源为卖家后台
     */
    public static final String STUDENT = "student";

    /**
     * 请求来源为运营后台
     */
    public static final String OPERATION_BACKSTAGE = "operationBackstage";
}
