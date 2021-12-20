package com.why.baseframework.constants;

/**
 * @Author W
 * @Description:
 * @Title: CorsConstants
 * @ProjectName WHY-Core
 * @Date 2021/4/16
 * @Company  WHY-Group
 */
public final class CorsConstants {

    /**
     * 跨域允许Headers
     */
    public  static final String ACCESS_ALLOW_HEADERS = "requestsource,RequestSource,Referer,sec-ch-ua,sec-ch-ua-mobile,Sec-Fetch-Dest,Sec-Fetch-Mode,Sec-Fetch-Site,User-Agent,Host,Authorization,Origin,X-Requested-With,Contenttype,Content-Type,Accept,Accept-Encoding,Accept-Language";

    /**
     * 跨域运行方法
     */
    public  static final String ACCESS_ALLOW_METHODS = "POST,GET,OPTIONS";

    /**
     *
     * @Title:
     * @Description:
     */
    private CorsConstants() {

    }
}
