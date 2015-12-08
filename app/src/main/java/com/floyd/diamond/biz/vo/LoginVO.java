package com.floyd.diamond.biz.vo;

/**
 * Created by floyd on 15-11-23.
 */
public class LoginVO {

    public long id;//用户id
    public String token;//token
    public String phoneNumber;//手机号码
    public String avartUrl;//头像地址
    public String nickname;//别名
    public int type;


    public String toString() {
        StringBuilder sb = new StringBuilder("id:").append(id+"");
        sb.append("---").append("phoneNumber:").append(phoneNumber);
        sb.append("---").append("avartUrl:").append(avartUrl);
        sb.append("---").append("nickname:").append(nickname);
        sb.append("---").append("accountType").append(type+"");
        return sb.toString();
    }

    public boolean isModel() {
        return type == 1;
    }
}

