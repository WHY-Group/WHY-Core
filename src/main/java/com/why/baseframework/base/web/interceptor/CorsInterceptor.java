package com.why.baseframework.base.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author W
 * @Description:
 * @Title: CorsInterceptor
 * @ProjectName WHY-Core
 * @Date 2021/4/16
 * @Company  WHY-Group
 */
@Slf4j
public class CorsInterceptor implements HandlerInterceptor {
    /**
     * 访问控制
     */
    private String accessControlAllowOrigin;

    public CorsInterceptor(String accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("uri {}", request.getRequestURI().toString());
        // 解决跨域
//        response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
//        response.setHeader("Access-Control-Allow-Headers", accessControlAllowOrigin);
//        response.setHeader("Access-Control-Allow-Methods", CorsConstants.ACCESS_ALLOW_METHODS);
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        return true;
    }
}
