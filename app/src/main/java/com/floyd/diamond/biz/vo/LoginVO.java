package com.floyd.diamond.biz.vo;

import com.floyd.diamond.biz.vo.mote.UserVO;

/**
 * Created by floyd on 15-11-23.
 */
public class LoginVO implements IKeepClassForProguard {

    public long id;//用户id
    public String token;//token
    public UserVO user;


    public String toString() {
        StringBuilder sb = new StringBuilder("id:").append(id+"");
        sb.append("---").append("phoneNumber:").append(user.phoneNumber);
        sb.append("---").append("avartUrl:").append(user.avartUrl);
        sb.append("---").append("nickname:").append(user.nickname);
        sb.append("---").append("accountType").append(user.type+"");
        return sb.toString();
    }

    public boolean isModel() {
        return user.type == 1;
    }
}

