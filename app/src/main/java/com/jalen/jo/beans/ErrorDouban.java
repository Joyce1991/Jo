package com.jalen.jo.beans;

/**
 * Created by jh on 2015/3/18.
 */
public class ErrorDouban {
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误的请求
     */
    private String request;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
