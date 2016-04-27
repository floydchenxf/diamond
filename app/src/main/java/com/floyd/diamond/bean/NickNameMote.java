package com.floyd.diamond.bean;

import com.floyd.diamond.utils.CommonUtil;

import java.util.List;

/**
 * Created by hy on 2016/4/6.
 */
public class NickNameMote {


    /**
     * id : 701
     * avatarUrl : http://qmmt2015.b0.upaiyun.com/2016/3/1/8a9aa763-02e7-496a-a995-b1bd1965f72d.png
     * userId : null
     * nickname : Mancy
     * phoneNumber : null
     * gender : null
     * age : null
     * heigth : null
     * shape : null
     * status : null
     * areaId : null
     * remindFee : null
     * taskFee : null
     * taskNum : null
     * goodEvalRate : null
     * birdthday : null
     * createTime : null
     * followNum : 73
     * isFollow : null
     * area : 上海
     * authenStatus : null
     * provinceName : null
     * provinceId : null
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private long id;
        private String avatarUrl;
        private Object userId;
        private String nickname;
        private Object phoneNumber;
        private Object gender;
        private Object age;
        private Object heigth;
        private Object shape;
        private Object status;
        private Object areaId;
        private Object remindFee;
        private Object taskFee;
        private Object taskNum;
        private Object goodEvalRate;
        private Object birdthday;
        private Object createTime;
        private int followNum;
        private boolean isFollow;
        private String area;
        private Object authenStatus;
        private Object provinceName;
        private Object provinceId;

        public void setId(long id) {
            this.id = id;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setPhoneNumber(Object phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public void setAge(Object age) {
            this.age = age;
        }

        public void setHeigth(Object heigth) {
            this.heigth = heigth;
        }

        public void setShape(Object shape) {
            this.shape = shape;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public void setAreaId(Object areaId) {
            this.areaId = areaId;
        }

        public void setRemindFee(Object remindFee) {
            this.remindFee = remindFee;
        }

        public void setTaskFee(Object taskFee) {
            this.taskFee = taskFee;
        }

        public void setTaskNum(Object taskNum) {
            this.taskNum = taskNum;
        }

        public void setGoodEvalRate(Object goodEvalRate) {
            this.goodEvalRate = goodEvalRate;
        }

        public void setBirdthday(Object birdthday) {
            this.birdthday = birdthday;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public void setFollowNum(int followNum) {
            this.followNum = followNum;
        }

        public void setIsFollow(boolean isFollow) {
            this.isFollow = isFollow;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setAuthenStatus(Object authenStatus) {
            this.authenStatus = authenStatus;
        }

        public void setProvinceName(Object provinceName) {
            this.provinceName = provinceName;
        }

        public void setProvinceId(Object provinceId) {
            this.provinceId = provinceId;
        }

        public long getId() {
            return id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getPreviewUrl() {
            return CommonUtil.getImage_400(this.avatarUrl);
        }

        public Object getUserId() {
            return userId;
        }

        public String getNickname() {
            return nickname;
        }

        public Object getPhoneNumber() {
            return phoneNumber;
        }

        public Object getGender() {
            return gender;
        }

        public Object getAge() {
            return age;
        }

        public Object getHeigth() {
            return heigth;
        }

        public Object getShape() {
            return shape;
        }

        public Object getStatus() {
            return status;
        }

        public Object getAreaId() {
            return areaId;
        }

        public Object getRemindFee() {
            return remindFee;
        }

        public Object getTaskFee() {
            return taskFee;
        }

        public Object getTaskNum() {
            return taskNum;
        }

        public Object getGoodEvalRate() {
            return goodEvalRate;
        }

        public Object getBirdthday() {
            return birdthday;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public int getFollowNum() {
            return followNum;
        }

        public boolean IsFollow() {
            return isFollow;
        }

        public String getArea() {
            return area;
        }

        public Object getAuthenStatus() {
            return authenStatus;
        }

        public Object getProvinceName() {
            return provinceName;
        }

        public Object getProvinceId() {
            return provinceId;
        }
    }
}
