package com.why.baseframework.util;

import com.why.baseframework.constants.RequestSource;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author Y
 * @Description: token工具类
 * @Title: TokenUtil
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
public class TokenUtil {

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    public static String createToken() {
        //token算法

        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 通过请求对象获取token
     *
     * @param request 请求对象
     * @return String
     * @author W
     * @date: 2021/4/20
     */
    public static String getTokenFromServlet(HttpServletRequest request) {
        String token = request.getHeader(RequestSource.TOKEN);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return token.replace(TOKEN_PREFIX, "");
    }
    /**
     * 通过请求对象获取token
     *
     * @param request 请求对象
     * @return String
     * @author W
     * @date: 2021/4/20
     */
    public static String getRequestSource(HttpServletRequest request) {
        String requestSource = request.getHeader(RequestSource.REQUEST_SOURCE);
        if (StringUtils.isBlank(requestSource)) {
            return null;
        }
        return requestSource;
    }

}
