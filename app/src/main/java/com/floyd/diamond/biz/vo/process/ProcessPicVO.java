package com.floyd.diamond.biz.vo.process;

import com.floyd.diamond.utils.CommonUtil;

import java.io.Serializable;

/**
 * Created by floyd on 15-12-13.
 * <p/>
 * "picList":[{"id":270,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/4c0d6ac7-eff5-40f3-aa04-90fc23e89cbb.jpeg",
 * "sort":0,"createTime":1445261931000,"updateTime":1445261931000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},{"id":273,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/22b7c019-c5aa-4902-a9dc-3ed82d30ae73.jpeg","sort":0,"createTime":1445261970000,"updateTime":1445261970000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},{"id":274,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/436d8a68-0ea9-4071-abf0-79a6a7d348ba.jpeg","sort":0,"createTime":1445261983000,"updateTime":1445261983000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},{"id":462,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/23/83679223-91d0-4e46-853e-e891403f714d.jpeg","sort":0,"createTime":1445568999000,"updateTime":1445568999000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},
 */
public class ProcessPicVO implements Serializable {
    public long id;
    public long moteTaskId;//模特儿接单的id
    public String imgUrl;
    public long createTime;
    public long updateTime;
    public long userId;
    public long taskId;//任务id

    public String getPreviewUrl() {
        return CommonUtil.getImage_200(this.imgUrl);
    }
}
