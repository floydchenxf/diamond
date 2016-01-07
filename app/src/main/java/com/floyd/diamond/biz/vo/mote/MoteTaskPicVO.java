package com.floyd.diamond.biz.vo.mote;

import com.floyd.diamond.biz.vo.IKeepClassForProguard;
import com.floyd.diamond.utils.CommonUtil;

/**
 * Created by floyd on 15-12-5.
 */
public class MoteTaskPicVO implements IKeepClassForProguard {
    public long id;
    public long moteTaskId;
    public String imgUrl;
    public int sort;
    public long createTime;
    public long updateTime;
    public long userId;
    public long taskId;
    public int upvote;
    public boolean isUpvoted;

    public String getPreviewImageUrl() {
        return CommonUtil.getImage_200(this.imgUrl);
    }

    public String getDetailImageUrl() {
        return CommonUtil.getImage_800(this.imgUrl);
    }
}
