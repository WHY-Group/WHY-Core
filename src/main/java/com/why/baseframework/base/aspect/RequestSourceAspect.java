package com.why.baseframework.base.aspect;

import com.why.baseframework.base.aop.SourceRequest;
import com.why.baseframework.base.web.exception.BusinessException;
import com.why.baseframework.constants.RequestSource;
import com.why.baseframework.enums.ErrCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author W
 * @description:
 * @title: RequestSourceAspect
 * @projectName tianrui
 * @date 2021年05月30日
 * @company WHY-Group
 */
@Component
@Aspect
@Slf4j
public class RequestSourceAspect {

    @Pointcut("@annotation(com.why.baseframework.base.aop.SourceRequest)")
    public void pointCut() {
    }

    /**
     * 在执行方法之前判断请求来源
     *
     * @return void
     * @param: joinPoint
     * @author W
     * @date 2021-05-30
     */
    @Before(value = "pointCut()")
    public void requestSourceAspect(JoinPoint joinPoint) throws BusinessException {
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SourceRequest annotation = signature.getMethod().getDeclaredAnnotation(SourceRequest.class);
        // 如果没有注解则直接返回
        if (ObjectUtils.allNull(annotation)) {
            return;
        }
        // 如果没值则直接放行
        String[] value = annotation.value();
        if (ObjectUtils.isEmpty(value) || (value.length == 1 && StringUtils.isBlank(value[0]))) {
            return;
        }
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.allNotNull(requestAttributes)) {
            Assert.notNull(requestAttributes, "requestAttributes对象为空");
            //从获取RequestAttributes中获取HttpServletRequest的信息
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            Assert.notNull(request, "请求对象为空");
            // 获取并判断请求来源
            String requestSource = request.getHeader(RequestSource.REQUEST_SOURCE);
            boolean flag = this.judgementRequestSource(value, requestSource);
            if (!flag) {
                throw new BusinessException(ErrCodeEnum.REQUEST_SOURCE.getCode(), "未知的请求来源");
            }
        }
    }

    /**
     * 判断请求来源
     *
     * @return boolean
     * @param: value
     * @param: requestSource
     * @author W
     * @date 2021-05-30
     */
    private boolean judgementRequestSource(String[] value, String requestSource) {
        for (String source : value) {
            if (StringUtils.equals(source, requestSource)) {
                return true;
            }
        }
        return false;
    }
}
