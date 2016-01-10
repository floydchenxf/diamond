package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/12.
 */
public class Model {

    /**
     * data : [{"id":256,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg","nickname":"时小差","followNum":null,"area":null},{"id":512,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/23/cd6d1c0a-09a1-42d7-925b-decb9655fb3d.jpeg","nickname":"豆豆","followNum":null,"area":null},{"id":768,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/4/87ca8d4a-7391-4833-9adf-b74ec549f28c.jpg","nickname":"care","followNum":null,"area":null},{"id":1024,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/2b1531a3-1e0d-4292-a065-5ff51169763a.jpg","nickname":"mary","followNum":null,"area":null},{"id":257,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/9154268f-47a3-4363-9f0d-2493824f1d25.jpeg","nickname":"小泼","followNum":null,"area":null},{"id":769,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/2/48be94b3-5610-4f0e-b50c-556d5b6bf307.jpg","nickname":"yuci","followNum":null,"area":null},{"id":1025,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/88ece653-32e4-41c2-b47d-fe5f6b31aa64.jpg","nickname":"♣️","followNum":null,"area":null},{"id":514,"avatarUrl":null,"nickname":null,"followNum":null,"area":null},{"id":770,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/2/30ea882b-05b9-4d9a-aa36-e4142645b9e0.jpeg","nickname":"豌豆公主","followNum":null,"area":null},{"id":1026,"avatarUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/1ff9ea92-e628-4369-8567-1d4a32721809.jpg","nickname":"红薯患者","followNum":null,"area":null}]
     * success : true
     */

    private boolean success;
    /**
     * id : 256
     * avatarUrl : http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg
     * nickname : 时小差
     * followNum : null
     * area : null
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
        private String nickname;
        private int followNum;
        private Object area;

        public void setId(long id) {
            this.id = id;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setFollowNum(int followNum) {
            this.followNum = followNum;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public long getId() {
            return id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public int getFollowNum() {
            return followNum;
        }

        public Object getArea() {
            return area;
        }
    }
}
