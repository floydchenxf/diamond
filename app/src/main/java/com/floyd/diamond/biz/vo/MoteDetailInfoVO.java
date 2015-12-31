package com.floyd.diamond.biz.vo;

import com.floyd.diamond.utils.CommonUtil;

import java.io.Serializable;

/**
 * Created by floyd on 15-12-5.
 */
public class MoteDetailInfoVO implements Serializable{
    public long id;
    public String phoneNumber;
    private String avartUrl;//头像
    public String nickname;//别名
    public String birdthdayStr;//生日
    public int gender; //性别
    public int height;//身高
    public int weight; //体重
    public String wangwang;//旺旺
    public String alipayId; //支付宝id
    public String alipayName;//支付宝名称
    public int status;//状态
    public String shopName;//店铺名称
    public String weixin;//微信
    public String address;//地址
    public String referee;
    public Double remindFee;
    public Double freezeFee;
    public int age;
    public int followNum;//粉丝数
    public boolean isFollow;
    private int jingyanzhi;
    private String manyidu;

    public int getJingyanzhi() {
        return jingyanzhi;
    }

    public void setJingyanzhi(int jingyanzhi) {
        this.jingyanzhi = jingyanzhi;
    }

    public String getManyidu() {
        return manyidu;
    }

    public void setManyidu(String manyidu) {
        this.manyidu = manyidu;
    }

    public String getPreviewImageUrl() {
        return CommonUtil.getImage_200(this.avartUrl);
    }

    public String getDetailImageUrl() {
        return CommonUtil.getImage_800(this.avartUrl);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvartUrl() {
        return avartUrl;
    }

    public void setAvartUrl(String avartUrl) {
        this.avartUrl = avartUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirdthdayStr() {
        return birdthdayStr;
    }

    public void setBirdthdayStr(String birdthdayStr) {
        this.birdthdayStr = birdthdayStr;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWangwang() {
        return wangwang;
    }

    public void setWangwang(String wangwang) {
        this.wangwang = wangwang;
    }

    public String getAlipayId() {
        return alipayId;
    }

    public void setAlipayId(String alipayId) {
        this.alipayId = alipayId;
    }

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public Double getRemindFee() {
        return remindFee;
    }

    public void setRemindFee(Double remindFee) {
        this.remindFee = remindFee;
    }

    public Double getFreezeFee() {
        return freezeFee;
    }

    public void setFreezeFee(Double freezeFee) {
        this.freezeFee = freezeFee;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }
}
