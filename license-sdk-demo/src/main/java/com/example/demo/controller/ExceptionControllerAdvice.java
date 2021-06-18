package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.bocloud.paas.license.sdk.core.exception.LicenseCheckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@code ExceptionControllerAdvice} 处理拦截的异常
 * <blockquote>MethodArgumentNotValidExceptionjson校验失败异常</blockquote>
 * <blockquote>ConstraintViolationException表单校验异常</blockquote>
 * <blockquote>BaseExceptionBase校验异常</blockquote>
 *
 * @author xiao yafeng
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * 拦截json校验失败异常
     *
     * @param e json校验异常
     * @return java.lang.String 错误信息
     * @author xiaoyafeng
     * @date 2020/12/16 14:35
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        return objectError.getDefaultMessage();
    }

    /**
     * 拦截baseException异常
     *
     * @param e baseException异常
     * @return CoreResult&lt;Object&gt; 返回系统异常
     * @author xiaoyafeng
     * @date 2020/12/16 14:38
     */
    @ExceptionHandler(LicenseCheckException.class)
    public Object handleException(LicenseCheckException e) {
        // 添加错误日志
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", e.getCode());
        jsonObject.put("message", e.getMessage());
        return jsonObject;
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleException(RuntimeException e) {
        log.error("系统异常：", e);
        return e;
    }

}