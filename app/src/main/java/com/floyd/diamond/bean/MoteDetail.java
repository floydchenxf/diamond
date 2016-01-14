package com.floyd.diamond.bean;

/**
 * Created by Administrator on 2015/12/30.
 */
public class MoteDetail {

    /**
     * followNum : null
     * isFollow : false
     * area :
     * height : 168
     * avartUrl : http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg
     * nickname : 时小差
     * age : 20
     * orderNum : 0
     * gender : 0
     * shape : null
     * goodeEvalRate : null
     */

    private DataEntity data;
    /**
     * data : {"followNum":null,"isFollow":false,"area":"","height":168,"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg","nickname":"时小差","age":20,"orderNum":0,"gender":0,"shape":null,"goodeEvalRate":null}
     * success : true
     */

    private boolean success;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataEntity getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class DataEntity {
        private int followNum;
        private boolean isFollow;
        private String area;
        private int height;
        private String avartUrl;
        private String nickname;
        private int age;
        private int orderNum;
        private int gender;
        private double shape;
        private Object goodeEvalRate;

        public void setFollowNum(int followNum) {
            this.followNum = followNum;
        }

        public void setIsFollow(boolean isFollow) {
            this.isFollow = isFollow;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setAvartUrl(String avartUrl) {
            this.avartUrl = avartUrl;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setShape(double shape) {
            this.shape = shape;
        }

        public void setGoodeEvalRate(Object goodeEvalRate) {
            this.goodeEvalRate = goodeEvalRate;
        }

        public int getFollowNum() {
            return followNum;
        }

        public boolean isIsFollow() {
            return isFollow;
        }

        public String getArea() {
            return area;
        }

        public int getHeight() {
            return height;
        }

        public String getAvartUrl() {
            return avartUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public int getAge() {
            return age;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public int getGender() {
            return gender;
        }

        public double getShape() {
            return shape;
        }

        public Object getGoodeEvalRate() {
            return goodeEvalRate;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
