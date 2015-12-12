package com.floyd.diamond.biz.vo;

import com.floyd.diamond.utils.CommonUtil;

/**
 * Created by floyd on 15-11-28.
 */
public class MoteInfoVO {
    public long id;
    public String nickname;//别名
    private String avatarUrl;//avatarUrl;//头像地址
    public int orderNum;
    public int fenNum;
    public int fee;
    public String authenPic1;//验证图片1
    public String authenPic2;//验证图片2
    public String authenPic3;//验证图片3
    public String authenStatus;//验证状态

    public String getHeadUrl() {
        return CommonUtil.getImage_100(this.avatarUrl);
    }

    public String getDetailUrl() {
        return CommonUtil.getImage_800(this.avatarUrl);
    }

}
