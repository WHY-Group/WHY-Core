package com.why.baseframework.base.web.controlleradvice;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author W
 * @description:
 * @title: ErrorControllerImpl
 * @projectName tianrui
 * @date 2021年06月06日
 * @company WHY-Group
 */
@Controller
public class ErrorControllerImpl implements ErrorController {
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public void handlerError(HttpServletRequest request) throws Throwable {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            throw (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
    }
}
