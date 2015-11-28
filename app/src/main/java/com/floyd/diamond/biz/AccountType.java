package com.floyd.diamond.biz;

/**
 * Created by floyd on 15-11-24.
 */
public enum AccountType {
    COMMON(1, "模特"), SELLER(2, "商家");

    public int code;
    public String name;

    AccountType(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
