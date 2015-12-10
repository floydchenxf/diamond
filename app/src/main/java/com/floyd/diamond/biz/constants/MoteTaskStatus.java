package com.floyd.diamond.biz.constants;

/**
 * Created by floyd on 15-12-11.
 */
public enum MoteTaskStatus {

    ALL(1, "全部"), DOING(2, "进行中"), CONFIRM(3,"待确认"), DONE(4,"已结束");

    public int code;
    public String name;

    MoteTaskStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
