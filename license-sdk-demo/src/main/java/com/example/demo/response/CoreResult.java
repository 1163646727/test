package com.example.demo.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * 统一返回格式
 *
 * @param <T> 返回结果数据类型
 * @author Administrator
 */
@Slf4j
@Data
public class CoreResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private String exceptionStackTrace;
    private String state;
    private int code;
    private T data;

    public CoreResult() {
    }

    private CoreResult(String state, int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.state = state;
        this.data = data;
    }

    private CoreResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private CoreResult(String state, int code, String message, String exceptionStackTrace, T data) {
        this.code = code;
        this.message = message;
        this.exceptionStackTrace = exceptionStackTrace;
        this.state = state;
        this.data = data;
    }

    /**
     * {@code 成功不返回data}
     *
     * @param <T> 返回数据类型
     * @return com.bolcoud.paas.license.config.common.response.CoreResult&lt;T&gt;
     * @version 1.0
     * @author lijinxuan
     * @date 2021/3/26 9:44
     */
    public static <T> CoreResult<T> success() {
        return new CoreResult<>();
    }

    public static <T> CoreResult<T> error(int code, String message) {
        return new CoreResult<>(code, message);
    }


    public static String getExceptionAllInformation(Exception ex) {
        if (null == ex) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        ex.printStackTrace(pw);
        String data = sw.getBuffer().toString();
        if (null != sw) {
            try {
                sw.close();
                if (null != pw) {
                    pw.close();
                }
            } catch (IOException e) {
                log.error("获取堆栈信息异常:{}", e.getMessage());
            }
        }
        return data;
    }
}