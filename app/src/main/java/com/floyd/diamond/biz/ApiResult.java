package com.floyd.diamond.biz;

/**
 * Created by floyd on 15-11-28.
 */
public class ApiResult<T> {
    public T result;
    public String msg;
    public int code;

    public boolean isSuccess() {
        return code == 200;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("code:").append(code).append("---msg:").append(msg).append("---result:").append(result);
        return sb.toString();
    }
}
