package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by hy on 2016/3/23.
 */
public class VipDays {

    /**
     * id : 0
     * userId : 0
     * vipTypeId : 1
     * expireDate : null
     * vipTypeName : 卖家秀
     * leftDays : 28
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private int id;
        private int userId;
        private int vipTypeId;
        private Object expireDate;
        private String vipTypeName;
        private int leftDays;

        public void setId(int id) {
            this.id = id;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setVipTypeId(int vipTypeId) {
            this.vipTypeId = vipTypeId;
        }

        public void setExpireDate(Object expireDate) {
            this.expireDate = expireDate;
        }

        public void setVipTypeName(String vipTypeName) {
            this.vipTypeName = vipTypeName;
        }

        public void setLeftDays(int leftDays) {
            this.leftDays = leftDays;
        }

        public int getId() {
            return id;
        }

        public int getUserId() {
            return userId;
        }

        public int getVipTypeId() {
            return vipTypeId;
        }

        public Object getExpireDate() {
            return expireDate;
        }

        public String getVipTypeName() {
            return vipTypeName;
        }

        public int getLeftDays() {
            return leftDays;
        }
    }
}
