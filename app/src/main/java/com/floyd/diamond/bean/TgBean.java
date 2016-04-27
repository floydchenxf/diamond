package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by hy on 2016/4/25.
 */
public class TgBean {

    /**
     * id : 35
     * title : 模特接单流程
     * imgUrl : http://qmmt2015.b0.upaiyun.com/2016/3/22/36d86230-efb8-4233-b423-f89b666b99b7.jpg
     * createTime : 1457690870000
     * updateTime : 1461035475000
     * type : 2
     * content : <p>    <img src="/myeditor/ueditor/jsp/upload/image/20160419/1461035369500012052.jpg" width="100%" alt="1461035369500012052.jpg"/></p><p>    <img src="/myeditor/ueditor/jsp/upload/image/20160419/1461035369504038007.jpg" width="100%" alt="1461035369504038007.jpg"/></p><p>    <img src="/myeditor/ueditor/jsp/upload/image/20160419/1461035369616062519.jpg" width="100%" alt="1461035369616062519.jpg"/></p><p></p><p></p><p>    <br/></p>
     * status : null
     * url : http://192.168.5.114/seller/back/publish/banner?id=35
     * urlType : 1
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
        private String title;
        private String imgUrl;
        private long createTime;
        private long updateTime;
        private int type;
        private String content;
        private Object status;
        private String url;
        private int urlType;

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setUrlType(int urlType) {
            this.urlType = urlType;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public int getType() {
            return type;
        }

        public String getContent() {
            return content;
        }

        public Object getStatus() {
            return status;
        }

        public String getUrl() {
            return url;
        }

        public int getUrlType() {
            return urlType;
        }
    }
}
