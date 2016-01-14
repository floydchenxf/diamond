package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by hy on 2016/1/13.
 */
public class ModelInfo {


    /**
     * data : [{"id":256,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg","userId":null,"nickname":"时小差","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":2,"isFollow":false,"area":null,"authenStatus":null},{"id":512,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/23/cd6d1c0a-09a1-42d7-925b-decb9655fb3d.jpeg","userId":null,"nickname":"豆豆","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":1,"isFollow":false,"area":null,"authenStatus":null},{"id":768,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/4/87ca8d4a-7391-4833-9adf-b74ec549f28c.jpg","userId":null,"nickname":"care","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":1,"isFollow":false,"area":null,"authenStatus":null},{"id":1024,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/2b1531a3-1e0d-4292-a065-5ff51169763a.jpg","userId":null,"nickname":"mary","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":1,"isFollow":false,"area":null,"authenStatus":null},{"id":257,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/9154268f-47a3-4363-9f0d-2493824f1d25.jpeg","userId":null,"nickname":"小泼","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":1,"isFollow":false,"area":null,"authenStatus":null},{"id":769,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/2/48be94b3-5610-4f0e-b50c-556d5b6bf307.jpg","userId":null,"nickname":"yuci","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":0,"isFollow":false,"area":null,"authenStatus":null},{"id":1025,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/88ece653-32e4-41c2-b47d-fe5f6b31aa64.jpg","userId":null,"nickname":"♣️","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":2,"isFollow":false,"area":null,"authenStatus":null},{"id":514,"avatarUrl":null,"userId":null,"nickname":null,"phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":0,"isFollow":false,"area":null,"authenStatus":null},{"id":770,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/2/30ea882b-05b9-4d9a-aa36-e4142645b9e0.jpeg","userId":null,"nickname":"豌豆公主","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":1,"isFollow":false,"area":null,"authenStatus":null},{"id":1026,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/1ff9ea92-e628-4369-8567-1d4a32721809.jpg","userId":null,"nickname":"红薯患者","phoneNumber":null,"gender":null,"age":null,"heigth":null,"shape":null,"status":null,"areaId":null,"remindFee":null,"taskFee":null,"taskNum":null,"goodEvalRate":null,"birdthday":null,"createTime":null,"followNum":2,"isFollow":false,"area":null,"authenStatus":null}]
     * success : true
     */

    private boolean success;
    /**
     * id : 256
     * avatarUrl : http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg
     * userId : null
     * nickname : 时小差
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
     * followNum : 2
     * isFollow : false
     * area : null
     * authenStatus : null
     */

    private List<DataEntity> data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
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
        private Object area;
        private Object authenStatus;

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

        public void setArea(Object area) {
            this.area = area;
        }

        public void setAuthenStatus(Object authenStatus) {
            this.authenStatus = authenStatus;
        }

        public long getId() {
            return id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
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

        public boolean isIsFollow() {
            return isFollow;
        }

        public Object getArea() {
            return area;
        }

        public Object getAuthenStatus() {
            return authenStatus;
        }
    }
}
