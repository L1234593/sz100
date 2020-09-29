package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HealExceptionAdvice {
    /**
     *  info:  打印日志，记录流程性的内容
     *  debug: 记录一些重要的数据 id, orderId, userId
     *  error: 记录异常的堆栈信息，代替e.printStackTrace();
     *  工作中不能有System.out.println(), e.printStackTrace();
     */
    private static final Logger log = LoggerFactory.getLogger(HealExceptionAdvice.class);

    /**
     * 自定义抛出的异常处理
     * @param he
     * @return
     */
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException he){
        // 我们自己抛出的异常，把异常信息包装下返回即可
        return new Result(false, he.getMessage());
    }

    /**
     * 所有未知的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生异常",e);
        return new Result(false, "发生未知错误，操作失败，请联系管理员");
    }
    /**
     * 密码错误
     */
    @ExceptionHandler(BadCredentialsException.class)
    public Result handBadCredentialsException(BadCredentialsException he){
        return handleUserPassword();
    }

    /**
     * 用户名不存在
     * @param he
     * @return
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result handInternalAuthenticationServiceException(InternalAuthenticationServiceException he){
        return handleUserPassword();
    }

    private Result handleUserPassword(){
        return new Result(false, "用户名或密码错误");
    }

    /**
     * 用户名不存在
     * @param he
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handAccessDeniedException(AccessDeniedException he){
        return new Result(false, "没有权限");
    }
}
