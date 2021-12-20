package com.why.baseframework.base.web.controlleradvice;

import com.why.baseframework.base.message.BaseMessage;
import com.why.baseframework.base.web.exception.BusinessException;
import com.why.baseframework.base.web.response.ResponseResult;
import com.why.baseframework.base.web.response.ResponseUtils;
import com.why.baseframework.enums.ErrCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.message.AuthException;
import java.nio.file.AccessDeniedException;

/**
 * @Author W
 * @Description:
 * @Title: CommonExceptionHandler
 * @ProjectName WHY-Core
 * @Date 2021/4/16 14:23
 * @Company WHY-Group
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CommonExceptionHandler {

    @Autowired
    private BaseMessage baseMessage;


    /**
     * 认证异常处理
     *
     * @param e 异常
     * @return ResponseResult<T>
     * @author W
     * @date: 2021/4/16
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public <T> ResponseResult<T> exceptionHandler(AuthException e) {
        // 401未授权
        return ResponseUtils.fail(ErrCodeEnum.AUTH.getCode(), e.getMessage());
    }

    /**
     * 处理方法参数校验异常
     *
     * @param e 方法参数校验异常
     * @return ResponseResult<T>
     * @author W
     * @date: 2021/4/19
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public <T> ResponseResult<T> exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = baseMessage.getMessageString("invalid.data");

        StringBuilder buf = new StringBuilder();
        int i = 0;
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (i > 0) {
                buf.append(";");
            }
            buf.append(fieldError.getDefaultMessage());

            i++;
        }

        errorMesssage += buf.toString();

        return ResponseUtils.fail(ErrCodeEnum.DATA_CHECK.getCode(), errorMesssage);
    }

    /**
     * 处理业务异常
     *
     * @param e 业务异常的处理
     * @return ResponseResult<T>
     * @author W
     * @date: 2021/4/19
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public <T> ResponseResult<T> exceptionHandler(BusinessException e) {
        log.warn(e.getErrMsg());
        return ResponseUtils.fail(e.getErrCode(), e.getErrMsg());
    }

    /**
     * 添加处理数据库字段重复的异常
     *
     * @return ResponseResult<T>
     * @param: e 唯一键重复
     * @author W
     * @date 2021-05-30
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public <T> ResponseResult<T> exceptionHandler(DuplicateKeyException e) {
        log.warn(e.getMessage());
        return ResponseUtils.fail(ErrCodeEnum.BUSINESS.getCode(), "数据库字段重复");
    }

    /**
     * @param e exception
     * @return ResponseResult<T>
     * @author W
     * @date: 2021/4/16
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public <T> ResponseResult<T> bindException(Exception e) {
        log.error("system error:", e);
        Throwable cause = this.getThrowableCause(e);
        return ResponseUtils.fail(ErrCodeEnum.DATA_CHECK.getCode(), cause.getMessage());
    }

    /**
     * 拦截所有的异常信息
     *
     * @param e 所有异常
     * @return ResponseResult<T>
     * @author W
     * @date: 2021/4/19
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public <T> ResponseResult<T> exceptionHandler(Exception e) {
        log.error("system error:", e);
        Throwable cause = this.getThrowableCause(e);
        return ResponseUtils.fail(ErrCodeEnum.BUSINESS.getCode(), cause.getMessage());
    }



    /**
     * 处理异常
     *
     * @param throwable 异常处理
     * @return Throwable
     * @author W
     * @date: 2021/4/19
     */
    private Throwable getThrowableCause(Throwable throwable) {
        if (throwable.getCause() != null) {
            return getThrowableCause(throwable.getCause());
        } else {
            return throwable;
        }
    }

}
