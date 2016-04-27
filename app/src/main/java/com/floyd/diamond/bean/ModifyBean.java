package com.floyd.diamond.bean;

/**
 * Created by hy on 2016/4/13.
 */
public class ModifyBean {

    /**
     * code : 3
     * success : false
     * msg : TOKEN楠岃瘉閿欒.
     */

    private boolean success;
    private String msg;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return (success+","+msg+","+data);
    }
}
