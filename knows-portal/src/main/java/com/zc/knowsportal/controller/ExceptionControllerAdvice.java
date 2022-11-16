package com.zc.knowsportal.controller;

import com.zc.knowsportal.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Cong
 * @ClassName ExceptionControllerAdvice
 * @Description 异常处理类 | @RestControllerAdvice表示当前类是统一处理所有控制器的功能的
 * @Date 16/11/2022  下午 1:45
 */

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    //@ExceptionHandler表示下面的方法是为控制器处理异常的
    @ExceptionHandler
    /**
     * 方法的名称实际上不强制,但是方法的参数,指定了当前方法处理的异常类型
     */
    public String handleServiceException(ServiceException e){
        log.error("发生业务异常",e);
        return e.getMessage();
    }

    @ExceptionHandler
    public String handleException(Exception e){
        log.error("发生其他异常", e);
        return e.getMessage();
    }
}
