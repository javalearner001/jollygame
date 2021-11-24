package com.sun.jollygame.entity.response;

/**
 * @author sunkai
 * @since 2021/3/17 11:14 上午
 */
public class BaseResult<T> {
    private int code;

    private int errorcode;

    private String errorMsg;

    private T data;

    public BaseResult(int errorcode, String errorMsg) {
        this.errorcode = errorcode;
        this.errorMsg = errorMsg;
        this.code = 200;
    }

    public BaseResult() {
        this.errorcode = 0;
        this.errorMsg = "success";
        this.code = 200;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
