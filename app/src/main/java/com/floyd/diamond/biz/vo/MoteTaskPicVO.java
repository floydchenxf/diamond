package com.floyd.diamond.biz.vo;

import com.floyd.diamond.utils.CommonUtil;

/**
 * Created by floyd on 15-12-5.
 */
public class MoteTaskPicVO {
    public long id;
    public long moteTaskId;
    private String imgUrl;
    public int sort;
    public long createTime;
    public long updateTime;
    public long userId;
    public long taskId;

    public String getPreviewImageUrl() {
        return CommonUtil.getImage_100(this.imgUrl);
    }

    public String getDetailImageUrl() {
        return CommonUtil.getImage_800(this.imgUrl);
    }
}
