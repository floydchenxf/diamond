package com.floyd.diamond.bean;

/**
 * Created by Administrator on 2016/1/3.
 */
public class MessageItem {

    /**
     * id : 3
     * title : 模特000144
     * imgUrl : http://qmmt2015.b0.upaiyun.com/2016/1/3/bbb7cf08-ad6d-4e5d-8d1c-fe0dd2ce9f61.jpg
     * createTime : 1448524335000
     * updateTime : 1451808006000
     * type : 2
     * content : <p><img src="/myeditor/ueditor/jsp/upload/image/20160103/1451806618242088360.jpg" title="1451806618242088360.jpg" alt="模特推荐.jpg"/></p>
     * status : 1
     * url :
     */

    private DataEntity data;
    /**
     * data : {"id":3,"title":"模特000144","imgUrl":"http://qmmt2015.b0.upaiyun.com/2016/1/3/bbb7cf08-ad6d-4e5d-8d1c-fe0dd2ce9f61.jpg","createTime":1448524335000,"updateTime":1451808006000,"type":2,"content":"<p><img src=\"/myeditor/ueditor/jsp/upload/image/20160103/1451806618242088360.jpg\" title=\"1451806618242088360.jpg\" alt=\"模特推荐.jpg\"/><\/p>","status":1,"url":""}
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
        private int id;
        private String title;
        private String imgUrl;
        private long createTime;
        private long updateTime;
        private int type;
        private String content;
        private int status;
        private String url;

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

        public void setStatus(int status) {
            this.status = status;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public int getStatus() {
            return status;
        }

        public String getUrl() {
            return url;
        }
    }
}
