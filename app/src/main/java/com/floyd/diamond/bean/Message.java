package com.floyd.diamond.bean;

import com.floyd.diamond.utils.CommonUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/12/5.
 */
public class Message {


    /**
     * data : [{"id":1,"title":"1.0上线啦","imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/26/9926766e-4553-4d1b-837e-7a0dc6b72305.jpg","createTime":1448525879000,"updateTime":1448525882000,"content":null}]
     * success : true
     */

    private boolean success;
    /**
     * id : 1
     * title : 1.0上线啦
     * imgUrl : http://qmmt2015.b0.upaiyun.com/2015/11/26/9926766e-4553-4d1b-837e-7a0dc6b72305.jpg
     * createTime : 1448525879000
     * updateTime : 1448525882000
     * content : null
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
        private int id;
        private String title;
        private String imgUrl;
        private long createTime;
        private long updateTime;
        private Object content;

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getPreviewUrl() {
            return CommonUtil.getImage_400(this.imgUrl);
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public void setContent(Object content) {
            this.content = content;
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

        public Object getContent() {
            return content;
        }
    }
}
