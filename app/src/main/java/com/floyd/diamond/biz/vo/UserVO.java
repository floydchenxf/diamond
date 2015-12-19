package com.floyd.diamond.biz.vo;

import com.floyd.diamond.utils.CommonUtil;

/**
 * Created by floyd on 15-12-18.
 */
public class UserVO {
    public long id;
    public String phoneNumber;
    public String avartUrl;
    public String password;
    public String nickname;
    public int gender;
    public String birdthdayStr;
    public int height;
    public int weight;
    public String areaId;
    public String wangwang;
    public String alipayId;
    public String weixin;
    public int type;

    public String getPreviewUrl() {
        return CommonUtil.getImage_200(avartUrl);
    }

    public String getGender() {
        if (gender == 1) {
            return "男";
        } else {
            return "女";
        }
    }
}
