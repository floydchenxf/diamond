package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/20.
 */
public class Care {

    /**
     * dataList : [{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/2b1531a3-1e0d-4292-a065-5ff51169763a.jpg","nickname":"mary","moteId":1024},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/5/abb733d8-106f-4c6b-8f7f-dd2ad79008fe.jpg","nickname":"小小","moteId":927},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/2/48be94b3-5610-4f0e-b50c-556d5b6bf307.jpg","nickname":"yuci","moteId":769},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/9154268f-47a3-4363-9f0d-2493824f1d25.jpeg","nickname":"小泼","moteId":257},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg","nickname":"时小差","moteId":256}]
     * pageNo : 1
     * pageSize : 10
     */

    private DataEntity data;
    /**
     * data : {"dataList":[{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/15/2b1531a3-1e0d-4292-a065-5ff51169763a.jpg","nickname":"mary","moteId":1024},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/5/abb733d8-106f-4c6b-8f7f-dd2ad79008fe.jpg","nickname":"小小","moteId":927},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/11/2/48be94b3-5610-4f0e-b50c-556d5b6bf307.jpg","nickname":"yuci","moteId":769},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/9154268f-47a3-4363-9f0d-2493824f1d25.jpeg","nickname":"小泼","moteId":257},{"avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/22/3cbedc7b-7911-4168-b49d-4dbd4e40b5c8.jpg","nickname":"时小差","moteId":256}],"pageNo":1,"pageSize":10}
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
        private int pageNo;
        private int pageSize;
        /**
         * avartUrl : http://qmmt2015.b0.upaiyun.com/2015/11/15/2b1531a3-1e0d-4292-a065-5ff51169763a.jpg
         * nickname : mary
         * moteId : 1024
         */

        private List<DataListEntity> dataList;

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setDataList(List<DataListEntity> dataList) {
            this.dataList = dataList;
        }

        public int getPageNo() {
            return pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public List<DataListEntity> getDataList() {
            return dataList;
        }

        public static class DataListEntity {
            private String avartUrl;
            private String nickname;
            private long moteId;

            public void setAvartUrl(String avartUrl) {
                this.avartUrl = avartUrl;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setMoteId(int moteId) {
                this.moteId = moteId;
            }

            public String getAvartUrl() {
                return avartUrl;
            }

            public String getNickname() {
                return nickname;
            }

            public long getMoteId() {
                return moteId;
            }
        }
    }
}
