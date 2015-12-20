package com.floyd.diamond.biz.vo;

import android.text.TextUtils;

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

    public int shape;//身形
    public long provinceId;
    public long cityId;
    public long districtId;

    public String provinceName;
    public String cityName;
    public String districtName;
    public String address;

    public int msgSwitch;
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

    public String getAddressSummary() {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(provinceName)) {
            sb.append(provinceName).append(" ");
        }

        if (!TextUtils.isEmpty(cityName)) {
            sb.append(cityName).append(" ");
        }

        if (!TextUtils.isEmpty(districtName)) {
            sb.append(districtName).append(" ");
        }
        String result = sb.toString();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

}
