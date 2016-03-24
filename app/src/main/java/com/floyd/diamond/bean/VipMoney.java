package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by hy on 2016/3/22.
 */
public class VipMoney {

    /**
     * id : 0
     * vipTypeIp : 0
     * cycle : 30
     * price : 1620000
     * discountPrice : 0
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
        private int vipTypeIp;
        private int cycle;
        private int price;
        private int discountPrice;

        public void setId(int id) {
            this.id = id;
        }

        public void setVipTypeIp(int vipTypeIp) {
            this.vipTypeIp = vipTypeIp;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setDiscountPrice(int discountPrice) {
            this.discountPrice = discountPrice;
        }

        public int getId() {
            return id;
        }

        public int getVipTypeIp() {
            return vipTypeIp;
        }

        public int getCycle() {
            return cycle;
        }

        public int getPrice() {
            return price;
        }

        public int getDiscountPrice() {
            return discountPrice;
        }
    }
}
