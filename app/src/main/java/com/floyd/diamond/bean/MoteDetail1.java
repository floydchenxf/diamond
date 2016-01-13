package com.floyd.diamond.bean;

import com.floyd.diamond.utils.CommonUtil;

/**
 * Created by Administrator on 2015/12/30.
 */
public class MoteDetail1 {

    /**
     * isFollow : false
     * followNum : null
     * avartUrl : http://qmmt2015.b0.upaiyun.com/2015/11/4/87ca8d4a-7391-4833-9adf-b74ec549f28c.jpg
     * height : 165
     * area :
     * nickname : care
     * age : 0
     * gender : 0
     * orderNum : 2
     * shape : null
     * goodeEvalRate : null
     */

    public boolean isFollow;
    public int followNum;
    public String avartUrl;
    public int height;
    public String area;
    public String nickname;
    public int age;
    public int gender;
    public int orderNum;
    private int shape;
    public float goodeEvalRate;

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public void setAvartUrl(String avartUrl) {
        this.avartUrl = avartUrl;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public boolean isIsFollow() {
        return isFollow;
    }

    public int getFollowNum() {
        return followNum;
    }

    public String getAvartUrl() {
        return CommonUtil.getImage_800(avartUrl);
    }

    public String getPreviewUrl() {
        return CommonUtil.getImage_200(this.avartUrl);
    }

    public int getHeight() {
        return height;
    }

    public String getArea() {
        return area;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public int getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}