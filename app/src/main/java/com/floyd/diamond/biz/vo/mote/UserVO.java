package com.floyd.diamond.biz.vo.mote;

import android.text.TextUtils;

import com.floyd.diamond.utils.CommonUtil;

import java.io.Serializable;

/**
 * Created by floyd on 15-12-18.
 */
public class UserVO implements Serializable {
    public long id;
    public String phoneNumber;
    public String avartUrl;
    public String password;
    public String nickname;
    public int gender;
    public String birdthdayStr;
    public long birdthday;
    public int height;
    public int weight;
    public String areaId;
    public String wangwang;
    public String alipayId;
    public String alipayName;
    public String weixin;
    public String qq;
    public String email;

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

    public int authenStatus; //验证状态
    public String authenPic1; //验证图片1
    public String authenPic2; //验证图片2
    public String authenPic3; //验证图片3
    public String idcardPic; //身份证图片
    public String idNumber; //身份证
    public String realName; //真实姓名

    public String returnItemMobile;//

    public String getPreviewUrl() {
        return CommonUtil.getImage_200(avartUrl);
    }

    public String getDetailUrl() {
        return CommonUtil.getImage_800(avartUrl);
    }

    public String getAuthPicPreview1() {
        return CommonUtil.getImage_400(authenPic1);
    }

    public String getAuthPicPreview2() {
        return CommonUtil.getImage_400(authenPic2);
    }

    public String getAuthPicPreview3() {
        return CommonUtil.getImage_400(authenPic3);
    }

    public String getIdCardPicPreview() {
        return CommonUtil.getImage_400(this.idcardPic);
    }

    public String getGender() {
        if (gender == 1) {
            return "男";
        } else if ( gender == 0){
            return "女";
        } else {
            return "未知";
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
