package com.floyd.diamond.biz.vo;

/**
 * Created by floyd on 15-11-23.
 */
public class LoginVO {

    public String userName; //显示名称
    public int accountType; //帐号类型
    public String accessToken;//token

    public String toString() {
        StringBuilder sb = new StringBuilder(userName);
        sb.append("---").append(accessToken).append("---").append(accountType);
        return sb.toString();
    }
}
