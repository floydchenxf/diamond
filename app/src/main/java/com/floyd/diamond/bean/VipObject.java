package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by hy on 2016/3/22.
 */
public class VipObject {

    /**
     * id : 1
     * vipPicUrl : http://qmmt2015.b0.upaiyun.com/2016/1/21/6f2ad349-d431-41e6-9771-a99b511c2838.jpg!v100
     * vipName : 卖家秀
     * vipDescription : 买家秀很好玩
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
        private String vipPicUrl;
        private String vipName;
        private String vipDescription;

        public void setId(int id) {
            this.id = id;
        }

        public void setVipPicUrl(String vipPicUrl) {
            this.vipPicUrl = vipPicUrl;
        }

        public void setVipName(String vipName) {
            this.vipName = vipName;
        }

        public void setVipDescription(String vipDescription) {
            this.vipDescription = vipDescription;
        }

        public int getId() {
            return id;
        }

        public String getVipPicUrl() {
            return vipPicUrl;
        }

        public String getVipName() {
            return vipName;
        }

        public String getVipDescription() {
            return vipDescription;
        }
    }
}
