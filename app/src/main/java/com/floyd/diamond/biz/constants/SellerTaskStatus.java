package com.floyd.diamond.biz.constants;

/**
 * Created by floyd on 15-12-26.
 */
public enum SellerTaskStatus {


    ALL(0,"全部"),DOING(1,"进行中"),CONFIRM(2,"待确认"),DONE(3,"已结束");

    public int code;
    public String name;

    SellerTaskStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }


}
